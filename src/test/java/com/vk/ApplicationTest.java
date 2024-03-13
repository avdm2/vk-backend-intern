package com.vk;

import static com.vk.RequestBodyForTests.POST_ALBUM;
import static com.vk.RequestBodyForTests.POST_POST;
import static com.vk.RequestBodyForTests.POST_SIGN_IN;
import static com.vk.RequestBodyForTests.POST_SIGN_UP;
import static com.vk.RequestBodyForTests.POST_USER;
import static com.vk.RequestBodyForTests.POST_USERNAME_ALREADY_EXISTS_SIGN_UP;
import static com.vk.RequestBodyForTests.POST_USER_CREDENTIAL_ARE_INCORRECT_SIGN_IN;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Проверка отправки некорректных запросов")
    @WithMockUser(username = "user", password = "pass", authorities = {"ROLE_ADMIN"})
    void testIncorrectRequests() throws Exception {
        mockMvc.perform(get("/api/incorrect/path")).andExpect(status().isNotFound());
        mockMvc.perform(post("/api/albums").content("incorrectContent")).andExpect(status().is4xxClientError());

        mockMvc.perform(post("/api/auth/signUp").content(POST_USERNAME_ALREADY_EXISTS_SIGN_UP).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/auth/signUp").content(POST_USERNAME_ALREADY_EXISTS_SIGN_UP).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andExpect(content().string("User with such username is already exists."));

        mockMvc.perform(post("/api/auth/signIn").content(POST_USER_CREDENTIAL_ARE_INCORRECT_SIGN_IN).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andExpect(content().string("Invalid username or password."));
    }

    @Test
    @DisplayName("Проверка роли ROLE_ADMIN")
    @WithMockUser(username = "user", password = "pass", authorities = {"ROLE_ADMIN"})
    void testRoleAdmin() throws Exception {
        mockMvc.perform(get("/api/posts")).andExpect(status().isOk());
        mockMvc.perform(get("/api/posts/20")).andExpect(status().isOk());
        mockMvc.perform(get("/api/posts/20/comments")).andExpect(status().isOk());
        mockMvc.perform(post("/api/posts").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(put("/api/posts/20").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(delete("/api/posts/1")).andExpect(status().isOk());

        mockMvc.perform(get("/api/users")).andExpect(status().isOk());
        mockMvc.perform(get("/api/users/1")).andExpect(status().isOk());
        mockMvc.perform(get("/api/users/1/comments")).andExpect(status().isOk());
        mockMvc.perform(post("/api/users").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(put("/api/users/1").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(delete("/api/users/1")).andExpect(status().isOk());

        mockMvc.perform(get("/api/albums")).andExpect(status().isOk());
        mockMvc.perform(get("/api/albums/20")).andExpect(status().isOk());
        mockMvc.perform(get("/api/albums/20/comments")).andExpect(status().isOk());
        mockMvc.perform(post("/api/albums").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(put("/api/albums/20").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(delete("/api/albums/1")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Проверка роли ROLE_POSTS")
    @WithMockUser(username = "user", password = "pass", authorities = {"ROLE_POSTS"})
    void testRolePosts() throws Exception {
        mockMvc.perform(get("/api/posts")).andExpect(status().isOk());
        mockMvc.perform(get("/api/posts/20")).andExpect(status().isOk());
        mockMvc.perform(get("/api/posts/20/comments")).andExpect(status().isOk());
        mockMvc.perform(post("/api/posts").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(put("/api/posts/20").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(delete("/api/posts/1")).andExpect(status().isOk());

        mockMvc.perform(get("/api/users")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/users/1")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/users/1/comments")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/users").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/users/1").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/users/1")).andExpect(status().isForbidden());

        mockMvc.perform(get("/api/albums")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/albums/20")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/albums/20/comments")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/albums").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/albums/20").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/albums/1")).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Проверка роли ROLE_POSTS_VIEWER")
    @WithMockUser(username = "user", password = "pass", authorities = {"ROLE_POSTS_VIEWER"})
    void testRolePostsViewer() throws Exception {
        mockMvc.perform(get("/api/posts")).andExpect(status().isOk());
        mockMvc.perform(get("/api/posts/20")).andExpect(status().isOk());
        mockMvc.perform(get("/api/posts/20/comments")).andExpect(status().isOk());
        mockMvc.perform(post("/api/posts").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/posts/20").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/posts/1")).andExpect(status().isForbidden());

        mockMvc.perform(get("/api/users")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/users/1")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/users/1/comments")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/users").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/users/1").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/users/1")).andExpect(status().isForbidden());

        mockMvc.perform(get("/api/albums")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/albums/20")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/albums/20/comments")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/albums").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/albums/20").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/albums/1")).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Проверка роли ROLE_USERS")
    @WithMockUser(username = "user", password = "pass", authorities = {"ROLE_USERS"})
    void testRoleUsers() throws Exception {
        mockMvc.perform(get("/api/posts")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/posts/20")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/posts/20/comments")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/posts").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/posts/20").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/posts/1")).andExpect(status().isForbidden());

        mockMvc.perform(get("/api/users")).andExpect(status().isOk());
        mockMvc.perform(get("/api/users/1")).andExpect(status().isOk());
        mockMvc.perform(get("/api/users/1/comments")).andExpect(status().isOk());
        mockMvc.perform(post("/api/users").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(put("/api/users/1").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(delete("/api/users/1")).andExpect(status().isOk());

        mockMvc.perform(get("/api/albums")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/albums/20")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/albums/20/comments")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/albums").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/albums/20").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/albums/1")).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Проверка роли ROLE_USERS_VIEWER")
    @WithMockUser(username = "user", password = "pass", authorities = {"ROLE_USERS_VIEWER"})
    void testRoleUsersViewer() throws Exception {
        mockMvc.perform(get("/api/posts")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/posts/20")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/posts/20/comments")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/posts").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/posts/20").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/posts/1")).andExpect(status().isForbidden());

        mockMvc.perform(get("/api/users")).andExpect(status().isOk());
        mockMvc.perform(get("/api/users/1")).andExpect(status().isOk());
        mockMvc.perform(get("/api/users/1/comments")).andExpect(status().isOk());
        mockMvc.perform(post("/api/users").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/users/1").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/users/1")).andExpect(status().isForbidden());

        mockMvc.perform(get("/api/albums")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/albums/20")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/albums/20/comments")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/albums").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/albums/20").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/albums/1")).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Проверка роли ROLE_ALBUMS")
    @WithMockUser(username = "user", password = "pass", authorities = {"ROLE_ALBUMS"})
    void testRoleAlbums() throws Exception {
        mockMvc.perform(get("/api/posts")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/posts/20")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/posts/20/comments")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/posts").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/posts/20").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/posts/1")).andExpect(status().isForbidden());

        mockMvc.perform(get("/api/users")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/users/1")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/users/1/comments")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/users").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/users/1").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/users/1")).andExpect(status().isForbidden());

        mockMvc.perform(get("/api/albums")).andExpect(status().isOk());
        mockMvc.perform(get("/api/albums/20")).andExpect(status().isOk());
        mockMvc.perform(get("/api/albums/20/comments")).andExpect(status().isOk());
        mockMvc.perform(post("/api/albums").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(put("/api/albums/20").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(delete("/api/albums/1")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Проверка роли ROLE_ALBUMS_VIEWER")
    @WithMockUser(username = "user", password = "pass", authorities = {"ROLE_ALBUMS_VIEWER"})
    void testRoleAlbumsViewer() throws Exception {
        mockMvc.perform(get("/api/posts")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/posts/20")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/posts/20/comments")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/posts").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/posts/20").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/posts/1")).andExpect(status().isForbidden());

        mockMvc.perform(get("/api/users")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/users/1")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/users/1/comments")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/users").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/users/1").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/users/1")).andExpect(status().isForbidden());

        mockMvc.perform(get("/api/albums")).andExpect(status().isOk());
        mockMvc.perform(get("/api/albums/20")).andExpect(status().isOk());
        mockMvc.perform(get("/api/albums/20/comments")).andExpect(status().isOk());
        mockMvc.perform(post("/api/albums").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/albums/20").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/albums/1")).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Проверка прав неавторизированного пользователя")
    void testAnonymousUser() throws Exception {
        mockMvc.perform(post("/api/auth/signUp").content(POST_SIGN_UP).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(post("/api/auth/signIn").content(POST_SIGN_IN).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        mockMvc.perform(get("/api/posts")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/posts/20")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/posts/20/comments")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/posts").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/posts/20").content(POST_POST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/posts/1")).andExpect(status().isForbidden());

        mockMvc.perform(get("/api/users")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/users/1")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/users/1/comments")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/users").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/users/1").content(POST_USER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/users/1")).andExpect(status().isForbidden());

        mockMvc.perform(get("/api/albums")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/albums/20")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/albums/20/comments")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/albums").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/albums/20").content(POST_ALBUM).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/albums/1")).andExpect(status().isForbidden());
    }
}
