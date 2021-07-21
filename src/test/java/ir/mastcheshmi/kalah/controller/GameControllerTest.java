package ir.mastcheshmi.kalah.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.mastcheshmi.kalah.controller.dto.MakeMoveResponseDTO;
import ir.mastcheshmi.kalah.controller.dto.NewGameResponseDTO;
import ir.mastcheshmi.kalah.domain.Game;
import ir.mastcheshmi.kalah.domain.KalahBoard;
import ir.mastcheshmi.kalah.service.GameService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameService gameService;

    @Test
    void createGame() throws Exception {
        Game createdGame = new Game(new KalahBoard());
        when(gameService.createGame())
                .thenReturn(createdGame);

        MvcResult mvcResult = mockMvc.perform(post("/games")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        NewGameResponseDTO newGameResponse = objectMapper.readValue(json, NewGameResponseDTO.class);
        assertThat(newGameResponse.getId())
                .isEqualTo(createdGame.getId());
        assertThat(newGameResponse.getUri())
                .isEqualTo("http://localhost/games/" + createdGame.getId());

        verify(gameService, times(1)).createGame();
    }

    @Test
    void makeMove() throws Exception {
        KalahBoard kalahBoard = new KalahBoard();
        kalahBoard.getPit(4).setStoneCount(5);
        kalahBoard.getPit(5).setStoneCount(8);
        kalahBoard.getPit(6).setStoneCount(5);
        Game selectedGame = new Game(kalahBoard);
        when(gameService.makeMove(1, 2))
                .thenReturn(selectedGame);

        MvcResult mvcResult = mockMvc.perform(put("/games/1/pits/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        MakeMoveResponseDTO gameResponse = objectMapper.readValue(json, MakeMoveResponseDTO.class);
        assertThat(gameResponse.getId())
                .isEqualTo(selectedGame.getId());
        JSONObject status = gameResponse.getStatus();
        assertThat(status.get("4"))
                .isEqualTo(5);
        assertThat(status.get("5"))
                .isEqualTo(8);
        assertThat(status.get("6"))
                .isEqualTo(5);

        verify(gameService, times(1)).makeMove(1, 2);
    }

    // TODO add test for thenThrow
}