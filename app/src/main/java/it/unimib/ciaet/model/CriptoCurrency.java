package it.unimib.ciaet.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class CriptoCurrency implements Parcelable {


    @PrimaryKey (autoGenerate = true)
    private long identificativo;
    private String simbolo;
    private double prezzo;
    private int id;

    public CriptoCurrency() {}

    public CriptoCurrency(String simbolo, double prezzo, int id) {
        this.simbolo = simbolo;
        this.prezzo = prezzo;
        this.id = id;
    }

    public long getIdentificativo() {
        return identificativo;
    }

    public void setIdentificativo(long identificativo) {
        this.identificativo = identificativo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }


    @Override
    public String toString() {
        return "CriptoCurrency{" +
                "identificativo=" + identificativo +
                ", simbolo='" + simbolo + '\'' +
                ", prezzo=" + prezzo +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CriptoCurrency that = (CriptoCurrency) o;
        return identificativo == that.identificativo && Double.compare(that.prezzo, prezzo) == 0 && id == that.id && Objects.equals(simbolo, that.simbolo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificativo, simbolo, prezzo, id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.identificativo);
        dest.writeString(this.simbolo);
        dest.writeDouble(this.prezzo);
        dest.writeInt(this.id);
    }

    public void readFromParcel(Parcel source) {
        this.identificativo = source.readLong();
        this.simbolo = source.readString();
        this.prezzo = source.readDouble();
        this.id = source.readInt();
    }

    protected CriptoCurrency(Parcel in) {
        this.identificativo = in.readLong();
        this.simbolo = in.readString();
        this.prezzo = in.readDouble();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<CriptoCurrency> CREATOR = new Parcelable.Creator<CriptoCurrency>() {
        @Override
        public CriptoCurrency createFromParcel(Parcel source) {
            return new CriptoCurrency(source);
        }

        @Override
        public CriptoCurrency[] newArray(int size) {
            return new CriptoCurrency[size];
        }
    };
}
