package ir.mastcheshmi.kalah.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ir.mastcheshmi.kalah.controller.dto.MakeMoveResponseDTO;
import ir.mastcheshmi.kalah.controller.dto.NewGameResponseDTO;
import ir.mastcheshmi.kalah.domain.Game;
import ir.mastcheshmi.kalah.service.GameService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/games")
@Api(value = "Provides APIs for creating a new game and doing operations on existing games")
public class GameController {

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping()
    @ApiOperation(value="Creates a new game")
    public ResponseEntity<NewGameResponseDTO> createGame() {
        Game game = this.gameService.createGame();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
                buildAndExpand(game.getId()).toUri();

        NewGameResponseDTO gameDTO = new NewGameResponseDTO(game.getId(), uri.toString());
        return ResponseEntity.created(uri).body(gameDTO);
    }

    @PutMapping("{gameId}/pits/{pitId}")
    @ApiOperation(value="Moves the stones in the given pit to the following pits based on the rules of the game")
    public MakeMoveResponseDTO makeMove(@PathVariable int gameId, @PathVariable int pitId) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        uri = StringUtils.substringBefore(uri, "/pits");
        Game game = this.gameService.makeMove(gameId, pitId);
        return new MakeMoveResponseDTO(game.getId(), uri, game.getKalahBoard().getStatus());
    }
}
