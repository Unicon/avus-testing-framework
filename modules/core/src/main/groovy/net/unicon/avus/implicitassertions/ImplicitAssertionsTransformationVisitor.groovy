package net.unicon.avus.implicitassertions

import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.control.SourceUnit

/**
 * Formally converts Service's authnResponse statements to be "implicit" assert statements.
 *
 * Call code for this package was taken almost directly from the base class.
 */
class ImplicitAssertionsTransformationVisitor extends geb.transform.implicitassertions.ImplicitAssertionsTransformationVisitor {

    private static final List<String> TRANSFORMED_CALLS_METHOD_NAMES = ["validator"]

    ImplicitAssertionsTransformationVisitor(SourceUnit sourceUnit) {
        super(sourceUnit)
    }

    @Override
    void visitField(FieldNode node) {
        if (node.static && node.initialExpression in ClosureExpression) {
            switch (node.name) {
                case 'validator':
                    transformEachStatement(node.initialExpression, true)
                    break
            }
        }
    }

    @Override
    void visitExpressionStatement(ExpressionStatement statement) {
        if (statement.expression in MethodCallExpression) {
            MethodCallExpression expression = statement.expression
            if (expression.methodAsString in TRANSFORMED_CALLS_METHOD_NAMES && expression.arguments in ArgumentListExpression) {
                ArgumentListExpression arguments = expression.arguments
                if (lastArgumentIsClosureExpression(arguments)) {
                    transformEachStatement(arguments.expressions.last(), false)
                }
            } else {
                compensateForSpockIfNecessary(expression)
            }
        }
    }

    void visitSpockValueRecordMethodCall(String name, List<Expression> arguments) {
        if (name in TRANSFORMED_CALLS_METHOD_NAMES) {
            if (!arguments.empty) {
                Expression lastArg = arguments.last()
                if (lastArg instanceof ClosureExpression) {
                    transformEachStatement(lastArg as ClosureExpression, false)
                }
            }
        }
    }

    private void transformEachStatement(ClosureExpression closureExpression, boolean appendTrueToNonAssertedStatements) {
        BlockStatement blockStatement = closureExpression.code
        ListIterator iterator = blockStatement.statements.listIterator()
        while (iterator.hasNext()) {
            iterator.set(maybeTransform(iterator.next(), appendTrueToNonAssertedStatements))
        }
    }

    private Statement maybeTransform(Statement statement, boolean appendTrueToNonAssertedStatements) {
        Statement result = statement
        Expression expression = getTransformableExpression(statement)
        if (expression) {
            result = transform(expression, statement, appendTrueToNonAssertedStatements)
        }
        result
    }

    private Expression getTransformableExpression(Statement statement) {
        if (statement in ExpressionStatement) {
            ExpressionStatement expressionStatement = statement
            if (!(expressionStatement.expression in DeclarationExpression)
                    && isTransformable(expressionStatement)) {
                return expressionStatement.expression
            }
        }
    }

    private Statement transform(Expression expression, Statement statement, boolean appendTrueToNonAssertedStatements) {
        Statement replacement

        Expression recordedValueExpression = createRuntimeCall("recordValue", expression)
        BooleanExpression booleanExpression = new BooleanExpression(recordedValueExpression)

        Statement retrieveRecordedValueStatement = new ExpressionStatement(createRuntimeCall("retrieveRecordedValue"))

        Statement withAssertion = new AssertStatement(booleanExpression)
        withAssertion.sourcePosition = expression
        withAssertion.statementLabel = (String) expression.getNodeMetaData("statementLabel")

        BlockStatement assertAndRetrieveRecordedValue = new BlockStatement()
        assertAndRetrieveRecordedValue.addStatement(withAssertion)
        assertAndRetrieveRecordedValue.addStatement(retrieveRecordedValueStatement)

        if (expression in MethodCallExpression) {
            MethodCallExpression rewrittenMethodCall = expression

            Statement noAssertion = new BlockStatement()
            noAssertion.addStatement(new ExpressionStatement(expression))
            if (appendTrueToNonAssertedStatements) {
                noAssertion.addStatement(new ExpressionStatement(ConstantExpression.TRUE))
            }
            StaticMethodCallExpression isVoidMethod = createRuntimeCall(
                    "isVoidMethod",
                    rewrittenMethodCall.objectExpression,
                    rewrittenMethodCall.method,
                    toArgumentArray(rewrittenMethodCall.arguments)
            )

            replacement = new IfStatement(new BooleanExpression(isVoidMethod), noAssertion, assertAndRetrieveRecordedValue)
        } else {
            replacement = assertAndRetrieveRecordedValue
        }

        replacement.sourcePosition = statement
        replacement
    }

    private StaticMethodCallExpression createRuntimeCall(String methodName, Expression... argumentExpressions) {
        ArgumentListExpression argumentListExpression = new ArgumentListExpression()
        for (Expression expression in argumentExpressions) {
            argumentListExpression.addExpression(expression)
        }

        new StaticMethodCallExpression(new ClassNode(Runtime), methodName, argumentListExpression)
    }

}
