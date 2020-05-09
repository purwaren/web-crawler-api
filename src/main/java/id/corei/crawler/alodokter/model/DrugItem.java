package id.corei.crawler.alodokter.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class DrugItem {
    private String id;

    @SerializedName(value = "postId")
    private String postId;

    @SerializedName(value = "post_title")

    private String postTitle;
    private String permalink;
    private String display;
}
