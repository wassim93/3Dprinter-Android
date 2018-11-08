package com.wassimapp.a3dprinter.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wassim on 18/02/2018.
 */

public class Model3D  implements Serializable{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("stlfilename")
    @Expose
    private String fileName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("imagename")
    @Expose
    private String imageName;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("visibility")
    @Expose
    private String visibility;
    @SerializedName("User")
    @Expose
    private User user;



    public Model3D() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Model3D{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                ", category='" + category + '\'' +
                ", user=" + user +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model3D model3D = (Model3D) o;

        return id == model3D.id;

    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
