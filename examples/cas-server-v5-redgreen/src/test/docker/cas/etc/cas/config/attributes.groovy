import java.util.*

def Map<String, List<Object>> run(final Object... args) {
    def uid = args[0]
    def logger = args[1]
    def casProperties = args[2]
    def casApplicationContext = args[3]

    logger.debug("[{}]: The received uid is [{}]", this.class.simpleName, uid)

    switch (uid) {
        case "casuser":
            return [username: [uid], givenName: "CAS", sn: "User", eduPersonAffiliation: ["community", "student"], employeeId: [12345], email: "casuser@exmaple.org"]
    }
}