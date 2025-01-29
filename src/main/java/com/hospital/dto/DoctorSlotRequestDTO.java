package com.hospital.dto;

import com.hospital.enums.DoctorSlots;

public class DoctorSlotRequestDTO {
	private String slot;
    private String time;
    private String date;
    private String docRegNum;

    

    public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDocRegNum() {
        return docRegNum;
    }

    public void setDocRegNum(String docRegNum) {
        this.docRegNum = docRegNum;
    }
}
