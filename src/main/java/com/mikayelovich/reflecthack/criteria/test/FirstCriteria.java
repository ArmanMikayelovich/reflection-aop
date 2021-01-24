package com.mikayelovich.reflecthack.criteria.test;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class FirstCriteria implements Serializable {


    private Integer documentId;

    private String documentName;


    private String documentUploadDateSTRING;

    private Integer organizationId;

    private List<Integer> loggedInBelongsUsers = new ArrayList<>();

    private List<String> testStringList = new ArrayList<>();
    private List<String> secondStringList = new ArrayList<>();
    private List<String> thirdStringList = new ArrayList<>();

}
