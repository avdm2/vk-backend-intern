package com.vk;

public class RequestBodyForTests {

    final static String POST_ALBUM = """
            {
                "userId": 11,
                "title": "My album"
            }
            """;

    final static String POST_USER = """
            {
                "name": "name",
                "username": "username",
                "email": "email@bk.ru",
                "address": {
                    "street": "Pokrovsky blvd",
                    "suite": "11",
                    "city": "Moscow",
                    "zipcode": "111111",
                    "geo": {
                        "lat": "ne znayu",
                        "lng": "smth"
                    }
                },
                "phone": "7-999-000-23-15",
                "website": "hse.ru",
                "company": {
                    "name": "VK",
                    "catchPhrase": "Backend internship",
                    "bs": "?"
                }
            }
            """;

    final static String POST_POST = """
            {
                "title": "III. Spring Boot Test",
                "body": "MockMvc testing",
                "userId": 100
            }
            """;

    final static String POST_SIGN_UP = """
            {
                "username": "user_test",
                "password": "pass",
                "role": "ROLE_ALBUMS"
            }
            """;

    final static String POST_USERNAME_ALREADY_EXISTS_SIGN_UP = """
            {
                "username": "user",
                "password": "pass",
                "role": "ROLE_ALBUMS"
            }
            """;

    final static String POST_SIGN_IN = """
            {
                "username": "user_test",
                "password": "pass"
            }
            """;

    final static String POST_USER_CREDENTIAL_ARE_INCORRECT_SIGN_IN = """
            {
                "username": "not_existing_user",
                "password": "pass"
            }
            """;
}
