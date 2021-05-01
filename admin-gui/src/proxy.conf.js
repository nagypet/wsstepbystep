const PROXY_CONFIG = [
    {
        context: [
            "/authenticate",
            "/admin",
            "/actuator",
            "/keystore",
            "/truststore",
            "/logout"
        ],
        target: "https://localhost:8400",
        secure: false
    }
]

module.exports = PROXY_CONFIG;