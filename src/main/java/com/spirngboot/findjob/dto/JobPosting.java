package com.spirngboot.findjob.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobPosting {
    private String title;
    private String company;
    private String link;

    public JobPosting(String title, String company, String link) {
        this.title = title;
        this.company = company;
        this.link = link;
    }
}