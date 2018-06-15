class SpockExampleSpec {
    def "example of basic spock spec: two plus two should equal four"() {
        given: "two inputs"
        int left = 2
        int right = 2

        when: "we add them together"
        int result = left + right

        then: "the right answer"
        result == 4
    }

    def "example of basic spock spec: length of Spock's and his friends' names"() {
        expect:
        name.size() == length

        where:
        name     | length
        "Spock"  | 5
        "Kirk"   | 4
        "Scotty" | 6
    }
}
