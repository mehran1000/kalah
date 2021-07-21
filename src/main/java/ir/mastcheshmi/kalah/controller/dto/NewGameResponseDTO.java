package ir.mastcheshmi.kalah.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Information about the created game")
public class NewGameResponseDTO {
    @ApiModelProperty("The game id")
    private int id;
    @ApiModelProperty("The uri of the created game resource")
    private String uri;

    public NewGameResponseDTO() {
    }

    public NewGameResponseDTO(int id, String uri) {
        this.id = id;
        this.uri = uri;
    }

    public int getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public void setId(int id) {
        this.id = id;
    }

}
