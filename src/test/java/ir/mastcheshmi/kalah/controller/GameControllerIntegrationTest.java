package ir.mastcheshmi.kalah.controller;

import ir.mastcheshmi.kalah.controller.dto.MakeMoveResponseDTO;
import ir.mastcheshmi.kalah.controller.dto.NewGameResponseDTO;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameControllerIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void createGame() {
        HttpEntity request = createRequest();
        ResponseEntity<NewGameResponseDTO> response =
             testRestTemplate.postForEntity("/games", request, NewGameResponseDTO.class);
        NewGameResponseDTO responseDTO = response.getBody();
        assertThat(responseDTO.getId())
                .isEqualTo(1);
        assertThat(responseDTO.getUri())
                .isEqualTo("http://localhost:" + port + "/games/" + responseDTO.getId());
        assertThat(response.getStatusCodeValue())
                .isEqualTo(Status.CREATED.getStatusCode());
    }

	@Nested
	@DisplayName("After game creation")
	class AfterGameCreation {
        int gameId = 1;

        @BeforeEach
        void createGame() {
            HttpEntity request = createRequest();
            ResponseEntity<NewGameResponseDTO> response =
                    testRestTemplate.postForEntity("/games", request, NewGameResponseDTO.class);
            NewGameResponseDTO responseDTO = response.getBody();
            gameId = responseDTO.getId();
        }

        @Test
        @DisplayName("When the pit id is invalid")
        void makeMove_invalidPitId() {
            String uri = createMakeMoveUri(gameId, 15);
            HttpEntity request = createRequest();
            ResponseEntity<JSONObject> response =
                    testRestTemplate.exchange(uri, HttpMethod.PUT, request, JSONObject.class);
            JSONObject errorBody = response.getBody();
            assertThat(response.getStatusCodeValue())
                    .isEqualTo(Status.BAD_REQUEST.getStatusCode());
            assertThat(errorBody.get("status"))
                    .isEqualTo(Status.BAD_REQUEST.getStatusCode());
            assertThat(errorBody.get("title"))
                    .isEqualTo("Invalid move");
            assertThat(errorBody.get("detail"))
                    .isEqualTo("The pit id must be between 1 and 14");
        }

        @Test
        @DisplayName("When the selected pit does not belong to current player")
        void makeMove_invalidPitIdForPlayer() {
            String uri = createMakeMoveUri(gameId, 8);
            HttpEntity request = createRequest();
            ResponseEntity<JSONObject> response =
                    testRestTemplate.exchange(uri, HttpMethod.PUT, request, JSONObject.class);
            JSONObject errorBody = response.getBody();
            assertThat(response.getStatusCodeValue())
                    .isEqualTo(Status.BAD_REQUEST.getStatusCode());
            assertThat(errorBody.get("status"))
                    .isEqualTo(Status.BAD_REQUEST.getStatusCode());
            assertThat(errorBody.get("title"))
                    .isEqualTo("Invalid move");
            assertThat(errorBody.get("detail"))
                    .isEqualTo("The pit does not belong to current player:PLAYER_1");
        }

        @Test
        @DisplayName("A valid move is followed by a move for an empty pit")
        void makeMove_validMove_followedByEmptyPitMove() {
            String uri = createMakeMoveUri(gameId, 6);
            HttpEntity request = createRequest();
            ResponseEntity<MakeMoveResponseDTO> response =
                    testRestTemplate.exchange(uri, HttpMethod.PUT, request, MakeMoveResponseDTO.class);
            MakeMoveResponseDTO body = response.getBody();

            assertThat(response.getStatusCodeValue())
                    .isEqualTo(Status.OK.getStatusCode());

            JSONObject boardStatus = body.getStatus();
            assertThat(boardStatus.get("7"))
                    .isEqualTo(1);
            assertThat(boardStatus.get("8"))
                    .isEqualTo(7);
            assertThat(boardStatus.get("6"))
                    .isEqualTo(0);

            // player 2 move
            uri = createMakeMoveUri(gameId, 8);
            testRestTemplate.exchange(uri, HttpMethod.PUT, request, JSONObject.class);

            // move for an empty pit
            uri = createMakeMoveUri(gameId, 6);
            ResponseEntity<JSONObject> emptyPitMove =
                    testRestTemplate.exchange(uri, HttpMethod.PUT, request, JSONObject.class);
            JSONObject errorBody = emptyPitMove.getBody();
            assertThat(emptyPitMove.getStatusCodeValue())
                    .isEqualTo(Status.BAD_REQUEST.getStatusCode());
            assertThat(errorBody.get("status"))
                    .isEqualTo(Status.BAD_REQUEST.getStatusCode());
            assertThat(errorBody.get("title"))
                    .isEqualTo("Invalid move");
            assertThat(errorBody.get("detail"))
                    .isEqualTo("The selected pit is empty");

        }
        private String createMakeMoveUri(int gameId, int pitId) {
            return String.format("/games/%d/pits/%d", gameId, pitId);
        }

    }

    private HttpEntity createRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity request = new HttpEntity<>( headers );
        return request;
    }


}
