package com.lotto;

public interface WireMockLottoResponse {

    default String retrieveNumbers(){
        return                          """
                                        [1, 2, 3, 4, 5, 6]
                                                                                 
                                        """.trim();
    }

    default String retrieveInputNumbers(){
        return """
                                {
                                "inputNumbers" : [1, 2, 3, 4 ,5 ,6 ]
                                }
                                """.trim();
    }

    default String retrieveNotFoundResponse(){
        return """
                               {
                               "message" : "Player not found for id notExistingId",
                               "status" : "NOT_FOUND"
                               }
                               """.trim();
    }

    default String retrieveSomeUserWithSomePassword(){
        return """
                        {
                            "email" : "email@gmail.com",
                            "password" : "somePassword"
                        }
                        """.trim();
    }
}
