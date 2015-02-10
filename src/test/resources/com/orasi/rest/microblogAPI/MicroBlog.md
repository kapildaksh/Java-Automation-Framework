# Microblog Snowcrash Example
Example Specification using the Snowcrash Parser

# Version [/version]

## Check Version [GET]
+ Response 200 (text/plain; charset=utf-8)

        v1.0

# Users [/users]

## Add User [POST]
+ Request Katherine (application/json; charset=utf-8)

        {
            "username": "katherine",
            "nickname": "kate",
            "password": "forceofnature"
        }

+ Request Humphrey (application/json; charset=utf-8)

        {
            "username": "humphrey",
            "nickname": "h264",
            "password": "correcthorsebatterystaple"
        }

+ Response 200 (application/json; charset=utf-8)

        {
            "type": "INFORMATIONAL",
            "message": "User added."
        }