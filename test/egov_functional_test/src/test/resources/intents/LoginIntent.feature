Feature: Login Intent Feature

  @Intent
  Scenario: LoginIntentTest
    And user on Login screen waits for kurnoolText to be visible
    Given user on Login screen types on username value ramana
    And user on Login screen types on password value demo
    And user on Login screen clicks on signIn


