package it.unimib.ciaet.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class GraphsApiResponse implements Parcelable {
    private String status;
    private int totalResults;
    private List<CriptoCurrency> currencies;

    public GraphsApiResponse(){}

    public GraphsApiResponse(String status, int totalResults, List<CriptoCurrency> currencies) {
        this.status = status;
        this.totalResults = totalResults;
        this.currencies = currencies;
    }

    public String getStatus() { return status;}

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<CriptoCurrency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<CriptoCurrency> currencies) {
        this.currencies = currencies;
    }

    @Override
    public String toString() {
        return "GraphsApiResponse{" +
                "status='" + status + '\'' +
                ", totalResults=" + totalResults +
                ", currencies=" + currencies +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeInt(this.totalResults);
        dest.writeTypedList(this.currencies);
    }

    public void readFromParcel(Parcel source) {
        this.status = source.readString();
        this.totalResults = source.readInt();
        this.currencies = source.createTypedArrayList(CriptoCurrency.CREATOR);
    }

    protected GraphsApiResponse(Parcel in) {
        this.status = in.readString();
        this.totalResults = in.readInt();
        this.currencies = in.createTypedArrayList(CriptoCurrency.CREATOR);
    }

    public static final Creator<GraphsApiResponse> CREATOR = new Creator<GraphsApiResponse>() {
        @Override
        public GraphsApiResponse createFromParcel(Parcel source) {
            return new GraphsApiResponse(source);
        }

        @Override
        public GraphsApiResponse[] newArray(int size) {
            return new GraphsApiResponse[size];
        }
    };
}
