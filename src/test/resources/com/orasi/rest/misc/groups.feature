Feature: Groups
    As a Microblog user
    I want to be able to create and join groups
    So that I can keep up with topics I am interested in

    @groups
    Scenario: Creating A Group
        Given Larry's account has been created
        And I am logged in as Larry
        When I send a request to Create Group Alpha
        Then I expect a response with code 200 OK
        When I send a request to Group Alpha Roster
        Then I expect a response matching Initial Roster

    @groups
    Scenario: Joining A Group
        Given the Alpha group has been created by Larry
        And Tom's account has been created
        And I am logged in as Tom
        When I send a request to Join Group Alpha
        Then I expect a response with code 200 OK
        When I send a request to Group Alpha Roster
        Then I expect a response matching Larry And Tom
        
    @groups @conflict
    Scenario: Joining A Group Again Should Not Occur
        Given the Alpha group has been created by Larry
        And I am logged in as Larry
        When I send a request to Join Group Alpha
        Then I expect a response with code 409 Conflict (Already Joined)
        When I send a request to Group Alpha Roster
        Then I expect a response matching Initial Roster

    @groups
    Scenario: Leaving A Group
        Given the Alpha group has been created by Larry
        And Tom has joined group Alpha
        And I am logged in as Tom
        When I send a request to Leave Group Alpha
        Then I expect a response with code 200 OK
        When I send a request to Group Alpha Roster
        Then I expect a response matching Initial Roster
        
    @groups
    Scenario: Disbanding A Group Automatically With No Members
        Given the Alpha group has been created by Larry
        And Tom has joined group Alpha
        And I am logged in as Larry
        When I send a request to Leave Group Alpha
        Then I expect a response with code 200 OK
        Given I am logged in as Tom
        When I send a request to Leave Group Alpha
        Then I expect a response with code 200 OK
        When I send a request to Group Alpha Roster
        Then I expect a response with code 404 Not Found

    @groups @conflict
    Scenario: Displacing A Group Should Not Occur
        Given the Alpha group has been created by Larry
        And Tom's account has been created
        And I am logged in as Tom
        When I send a request to Create Group Alpha
        Then I expect a response with code 409 Conflict (Already Exists)

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

    @groups @security
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
        Then I expect a response with code 404 Forbidden

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