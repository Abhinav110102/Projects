/*
 * Copyright (c) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sports.model


import com.example.rxpress10.R

/**
 * Data model for each row of the RecyclerView
 */
// Medication Object that takes in the parameters from user input
data class Medication(
    val name: String,
    val dosage: Double,
    val units: String,
    val Frequency: Int,
    val administered: String,
    val other: String? = null,
    val rxNum: String? = null
)
