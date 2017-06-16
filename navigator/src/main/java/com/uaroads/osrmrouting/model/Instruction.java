package com.uaroads.osrmrouting.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.uaroads.osrmrouting.R;

import java.util.HashMap;
import java.util.Map;

public class Instruction implements Parcelable {

    public static final int DIRECTION_INDEX = 0;
    public static final int STREET_NAME_INDEX = 1;
    public static final int LENGTH_INDEX = 2;
    public static final int POSITION_INDEX = 3;
    public static final int TIME_INDEX = 4;

    public static Map<String, Integer> mapInstructionToResourceId;

    static {
        mapInstructionToResourceId = new HashMap<>();
        mapInstructionToResourceId.put("1", R.drawable.dir_straight);
        mapInstructionToResourceId.put("2", R.drawable.dir_righter);
        mapInstructionToResourceId.put("3", R.drawable.dir_right);
        mapInstructionToResourceId.put("4", R.drawable.dir_sharp_right);
        mapInstructionToResourceId.put("5", R.drawable.dir_turnaround);
        mapInstructionToResourceId.put("6", R.drawable.dir_slightly_left);
        mapInstructionToResourceId.put("7", R.drawable.dir_left);
        mapInstructionToResourceId.put("8", R.drawable.dir_lefter);
        mapInstructionToResourceId.put("9", R.drawable.dir_straight);
        mapInstructionToResourceId.put("10", R.drawable.dir_straight); // ?
        mapInstructionToResourceId.put("11-1", R.drawable.dir_circle);
        mapInstructionToResourceId.put("11-2", R.drawable.dir_circle);
        mapInstructionToResourceId.put("11-3", R.drawable.dir_circle);
        mapInstructionToResourceId.put("11-4", R.drawable.dir_circle);
        mapInstructionToResourceId.put("11-5", R.drawable.dir_circle);
        mapInstructionToResourceId.put("11-6", R.drawable.dir_circle);
        mapInstructionToResourceId.put("11-7", R.drawable.dir_circle);
        mapInstructionToResourceId.put("11-8", R.drawable.dir_circle);
        mapInstructionToResourceId.put("11-9", R.drawable.dir_circle);
        mapInstructionToResourceId.put("15", R.drawable.ic_finish);
    }

    public String direction;
    public String currentStreetName;
    public String nextStreetName;
    public long distanceToDirection;
    public long timeToDirection;
    public int positionInPolyline;
    public boolean isLastInstruction;

    public int getInstructionImageId() {
        return mapInstructionToResourceId.get(direction);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(direction);
        parcel.writeString(currentStreetName);
        parcel.writeString(nextStreetName);
        parcel.writeLong(distanceToDirection);
        parcel.writeLong(timeToDirection);
        parcel.writeInt(positionInPolyline);
        parcel.writeByte((byte) (isLastInstruction ? 1 : 0));
    }

    public static final Creator<Instruction> CREATOR = new Creator<Instruction>() {
        @Override
        public Instruction createFromParcel(Parcel in) {
            final Instruction instruction = new Instruction();
            instruction.direction = in.readString();
            instruction.currentStreetName = in.readString();
            instruction.nextStreetName = in.readString();
            instruction.distanceToDirection = in.readLong();
            instruction.timeToDirection = in.readLong();
            instruction.positionInPolyline = in.readInt();
            instruction.isLastInstruction = in.readByte() != 0;
            return instruction;
        }

        @Override
        public Instruction[] newArray(int size) {
            return new Instruction[size];
        }
    };
}
