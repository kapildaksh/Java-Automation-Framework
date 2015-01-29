Feature: Group Administration
    As a Microblog Group Administrator
    I want to be able to limit users belonging to my group

    @groups
    Scenario: Setting Mutual Exclusivity On A Group
        Given the following groups:
            |   Alpha   |   Larry Tom           |
            |   Omega   |   Omega               |
        And I am logged in as Larry
        When I send a request to Set Mutual Exclusion
        And I define replacements:
            |   first   |   alpha               |
            |   second  |   omega               |
        Then I expect a response with code 200 OK

    @groups @conflict
    Scenario: Mutual Exclusivity On A Group
        Given the following groups:
            |   Alpha   |   Larry Tom           |
            |   Omega   |   Omega               |
        And I am logged in as Larry
        And Alpha and Omega are mutually exclusive
        And I am logged in as Omega
        When I send a request to Join Group Alpha
        Then I expect a response with code 409 Conflict (Mutual Exclusion)

    @groups @security @conflict
    Scenario: Releasing Mutual Exclusivity On A Group Requires Ownership
        Given the following groups:
            |   Alpha   |   Larry Tom           |
            |   Omega   |   Omega               |
        And I am logged in as Larry
        And Alpha and Omega are mutually exclusive
        And I am logged in as Omega
        When I send a request to Unset Mutual Exclusion
        And I define replacements:
            |   first   |   alpha               |
            |   second  |   omega               |
        Then I expect a response with code 403 Forbidden
        When I send a request to Join Group Alpha
        Then I expect a response with code 409 Conflict (Mutual Exclusion)

    @groups
    Scenario: Releasing Mutual Exclusivity On A Group
        Given the following groups:
            |   Alpha   |   Larry Tom           |
            |   Omega   |   Omega               |
        And I am logged in as Larry
        And Alpha and Omega are mutually exclusive
        And I am logged in as Larry
        When I send a request to Unset Mutual Exclusion
        And I define replacements:
            |   first   |   alpha               |
            |   second  |   omega               |
        Then I expect a response with code 200 OK
        Given I am logged in as Omega
        When I send a request to Join Group Alpha
        Then I expect a response with code 200 OK

    @groups @security
    Scenario: Mutual Exclusivity Is Mutual
        Given the following groups:
            |   Alpha   |   Larry Tom           |
            |   Omega   |   Omega               |
        And I am logged in as Larry
        And Alpha and Omega are mutually exclusive
        Given I am logged in as Tom
        When I send a request to Join Group Omega
        Then I expect a response with code 409 Conflict (Mutual Exclusion)

    @groups @security
    Scenario: Unsetting Mutual Exclusivity Doesn't Affect Other Side
        Given the following groups:
            |   Alpha   |   Larry Tom           |
            |   Omega   |   Omega               |
        And I am logged in as Larry
        And Alpha and Omega are mutually exclusive
        Given I am logged in as Omega
        When I send a request to Unset Mutual Exclusion
        And I define replacements:
            |   first   |   omega               |
            |   second  |   alpha               |
        Then I expect a response with code 200 OK
        When I send a request to Join Group Alpha
        Then I expect a response with code 409 Conflict (Mutual Exclusion)

    @groups
    Scenario: Unsetting Mutual Exclusivity When Not Set
        Given the following groups:
            |   Alpha   |   Larry Tom           |
            |   Omega   |   Omega               |
        And I am logged in as Larry
        When I send a request to Unset Mutual Exclusion
        And I define replacements:
            |   first   |   alpha               |
            |   second  |   omega               |
        Then I expect a response with code 200 OK