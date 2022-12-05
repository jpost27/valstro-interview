package model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class StarWarsSearchResponse implements Serializable {

    // Message index. If this matches resultCount, it is the last message in a sequence!
    private Integer page;

    // total number of results to be sent to client
    private Integer resultCount;

    // name matched against query
    private String name;

    // array of comma-separated strings, showing a character's filmography
    private String[] films;

    // error text will always be populated for errors
    private String error;

    public StarWarsSearchResponse(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("page")) {
            setPage(jsonObject.getInt("page"));
        }
        if (jsonObject.has("resultCount")) {
            setResultCount(jsonObject.getInt("resultCount"));
        }
        if (jsonObject.has("name")) {
            setName(jsonObject.getString("name"));
        }
        if (jsonObject.has("films")) {
            this.films = jsonObject.getString("films").split(", ");
        }
        if (jsonObject.has("error")) {
            setError(jsonObject.getString("error"));
        }
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getFilms() {
        return films;
    }

    public void setFilms(String[] films) {
        this.films = films;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StarWarsSearchResponse that = (StarWarsSearchResponse) o;
        return Objects.equals(page, that.page) &&
                Objects.equals(resultCount, that.resultCount) &&
                Objects.equals(name, that.name) &&
                Arrays.equals(films, that.films) &&
                Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(page, resultCount, name, error);
        result = 31 * result + Arrays.hashCode(films);
        return result;
    }

    @Override
    public String toString() {
        return "StarWarsResponse{" +
                "page=" + page +
                ", resultCount=" + resultCount +
                ", name='" + name + '\'' +
                ", films=" + Arrays.toString(films) +
                ", error='" + error + '\'' +
                '}';
    }
}
