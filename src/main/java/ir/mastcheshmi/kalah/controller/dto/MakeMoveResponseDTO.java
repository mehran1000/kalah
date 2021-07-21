package ir.mastcheshmi.kalah.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.json.simple.JSONObject;

@ApiModel(description = "Information about the game after the move")
public class MakeMoveResponseDTO {
    @ApiModelProperty("The game id")
    private int id;
    @ApiModelProperty("The uri of the game resource")
    private String uri;
    @ApiModelProperty("The status of the Kalaboard(The number of stones in each pit)")
    private JSONObject status;

    public MakeMoveResponseDTO() {
    }

    public MakeMoveResponseDTO(int id, String uri, JSONObject status) {
        this.id = id;
        this.uri = uri;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public JSONObject getStatus() {
        return status;
    }

}
