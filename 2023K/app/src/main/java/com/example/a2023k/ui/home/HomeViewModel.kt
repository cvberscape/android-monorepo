package com.example.a2023k.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "HEART RATE"
    }
    val text: LiveData<String> = _text

    private val _textbpm = MutableLiveData<String>().apply {
        value = "N/A"
    }
    val textbpm: LiveData<String> = _textbpm

    private val _textbpmstatic = MutableLiveData<String>().apply {
        value = "BPM"
    }
    val textbpmstatic: LiveData<String> = _textbpmstatic

    private val _text2 = MutableLiveData<String>().apply {
        value = "RESPIRATORY RATE"
    }
    val text2: LiveData<String> = _text2

    private val _textrr = MutableLiveData<String>().apply {
        value = " N/A"
    }
    val textrr: LiveData<String> = _textrr

    private val _textrrstatic = MutableLiveData<String>().apply {
        value = "  BPM"
    }
    val textrrstatic: LiveData<String> = _textrrstatic

    private val _textmapstatic = MutableLiveData<String>().apply {
        value = "CURRENT LOCATION"
    }
    val textmapstatic: LiveData<String> = _textmapstatic
}