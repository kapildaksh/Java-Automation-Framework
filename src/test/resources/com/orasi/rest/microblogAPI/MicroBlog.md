FORMAT: 1A

# Microblog Snowcrash Example
Example Specification using the Snowcrash Parser

# Version [/version]

## Check Version [GET]
+ Response 200 (text/plain; charset=utf-8)

        v1.0

# Login [/login]

## Authenticate [POST]
+ Request Katherine (application/json; charset=utf-8)

    + Headers

            Authorization: Basic a2F0aGVyaW5lOmZvcmNlb2ZuYXR1cmU=

+ Response 200 (text/plain; charset=utf-8)

        Login Successful

# Users [/users]

## Add User [POST]
+ Request Katherine (application/json; charset=utf-8)

        {
            "username": "katherine",
            "nickname": "kate",
            "password": "forceofnature"
        }

+ Response 200 (application/json; charset=utf-8)

        {
            "type": "INFORMATIONAL",
            "message": "User added."
        }

+ Request Humphrey (application/json; charset=utf-8)

        {
            "username": "humphrey",
            "nickname": "humphrey",
            "password": "correcthorsebatterystaple"
        }

+ Response 200 (application/json; charset=utf-8)

        {
            "type": "INFORMATIONAL",
            "message": "User added."
        }

+ Request Variable (application/json; charset=utf-8)

        {
            "username": "${username}",
            "nickname": "${nickname}",
            "password": "${password}"
        }
        
+ Response 200 (application/json; charset=utf-8)

        {
            "type": "INFORMATIONAL",
            "message": "User added."
        }

# User [/users/{user}]

+ Parameters

    + user (string, `katherine`) ... A user name

## Check [GET]

+ Response 200 (application/json; charset=utf-8)

        {
            "username": "${username}",
            "nickname": "${nickname}"
        }