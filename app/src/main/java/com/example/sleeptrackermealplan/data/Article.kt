package com.example.sleeptrackermealplan.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val title: String,
    val description: String,
    val imageUrl: String,
    val articleUrl: String
) : Parcelable
