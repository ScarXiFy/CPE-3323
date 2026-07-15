package com.usc.cems.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.usc.cems.data.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val allEvents = listOf(
        Event(
            id = "1",
            title = "Intro to UI/UX with Figma",
            category = "Workshop",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuADwKz2uH2H6V2HKSWMXaA_TQ6EVx90P06o5aXAsidYR953YbUl33-1ojwUjbv6tKqe8IqobWimdXrNmKWzKYZmAmCBYFP_SIebaLrvgwaKrCGu0JmyoEPUs0_1WoGBhW-BrmYcLIkiGjg0T8OTKT1qKP1PFiXFa0mUCCR6Z9fHndvGVKKb7_a2MuHLBijTcjh6rNxaMG9nKW6y-bdTXf31se5aoDf9xTHQ_FTdyYz0EV_7-BIwAURx7Q",
            dateTime = "Oct 12 • 2:30 PM - 4:00 PM",
            location = "Engineering Building, Lab 402",
            spotsLeft = "24 spots left"
        ),
        Event(
            id = "2",
            title = "Varsity Basketball: Finals",
            category = "Sports",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBCQTCcNBF1wOpcIMotc_rjOe1Wi_hASDSx7C9F5Qv7Wj5_aaOe9tHGg1hKNDgIi692VsHTgsyqk5uUpN7ehrTVUeCyedT8yHpjf9m3OiaAZAoSRvl0T3WbFF43Tk0q1KTN7Q-jkIAj2FcqhnuAMd4tUPaWX7sXYuMrktXKPc7cvzDO00XE7WWAnyi5EFRXYbdqslFY6DnW41DCgg73eURZpiBGZam_KRFVPh10x28JBy-EY3TbdDNlmg",
            dateTime = "Oct 14 • 6:00 PM",
            location = "Main Sports Complex"
        ),
        Event(
            id = "3",
            title = "International Food Festival",
            category = "Cultural",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDfXIpxvvvQAo3BlRv_6tTRawA7Jqo9rfofDe8cZuV4s4WENfgeuXeEK4TME-P9hMqGIqZtWQwOgcald9_nr2v753ott6XoffJVLaDaLmK_g5VKOFEraYdkwqEhSKnzmvSkKpp0UXRqQa1GTxDGVTodInPhzNKPym0bydsLZj81PYRop5ReUFMFej3ctFu-I_tTNRZmeQtVnIrvqk5llp5AfXfQyIuCZIYFRm1FybIoskqnr3bubaUpPg",
            dateTime = "Oct 15 • 11:00 AM - 3:00 PM",
            location = "Central Campus Green"
        )
    )

    var searchQuery by mutableStateOf("")
        private set

    var selectedCategory by mutableStateOf("All Events")
        private set

    val categories = listOf("All Events", "Academic", "Sports", "Cultural", "Workshop")

    val events: List<Event>
        get() = allEvents.filter { event ->
            val matchesCategory = selectedCategory == "All Events" || event.category.equals(selectedCategory, ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() || event.title.contains(searchQuery, ignoreCase = true) || event.location.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun selectCategory(category: String) {
        selectedCategory = category
    }
}
