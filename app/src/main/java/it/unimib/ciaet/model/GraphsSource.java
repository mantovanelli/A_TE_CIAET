package it.unimib.ciaet.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Class to represent the source of news of NewsAPI.org (https://newsapi.org).
 */
public class GraphsSource implements Parcelable {
    private String name;

    public GraphsSource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphsSource that = (GraphsSource) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "NewsSource{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
    }

    protected GraphsSource(Parcel in) {
        this.name = in.readString();
    }

    public static final Creator<GraphsSource> CREATOR = new Creator<GraphsSource>() {
        @Override
        public GraphsSource createFromParcel(Parcel source) {
            return new GraphsSource(source);
        }

        @Override
        public GraphsSource[] newArray(int size) {
            return new GraphsSource[size];
        }
    };
}
