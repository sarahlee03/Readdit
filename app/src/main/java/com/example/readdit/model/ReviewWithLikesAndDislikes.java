package com.example.readdit.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class ReviewWithLikesAndDislikes {
    public Review review;
    public List<String> likes;
    public List<String> dislikes;
}
