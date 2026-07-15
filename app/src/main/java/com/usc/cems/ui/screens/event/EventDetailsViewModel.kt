package com.usc.cems.ui.screens.event

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class DetailedEvent(
    val id: String,
    val title: String,
    val category: String,
    val imageUrl: String,
    val dateTime: String,
    val location: String,
    val spotsLeft: String? = null,
    val description: String,
    val organizerName: String,
    val organizerLogo: String,
    val attendingCount: String,
    val registrationStatus: String
)

@HiltViewModel
class EventDetailsViewModel @Inject constructor() : ViewModel() {

    private val detailedEvents = listOf(
        DetailedEvent(
            id = "1",
            title = "Intro to UI/UX with Figma",
            category = "Workshop",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuADwKz2uH2H6V2HKSWMXaA_TQ6EVx90P06o5aXAsidYR953YbUl33-1ojwUjbv6tKqe8IqobWimdXrNmKWzKYZmAmCBYFP_SIebaLrvgwaKrCGu0JmyoEPUs0_1WoGBhW-BrmYcLIkiGjg0T8OTKT1qKP1PFiXFa0mUCCR6Z9fHndvGVKKb7_a2MuHLBijTcjh6rNxaMG9nKW6y-bdTXf31se5aoDf9xTHQ_FTdyYz0EV_7-BIwAURx7Q",
            dateTime = "Oct 12 • 2:30 PM - 4:00 PM",
            location = "Engineering Building, Lab 402",
            spotsLeft = "24 spots left",
            description = "Learn the fundamentals of designing clean, interactive user interfaces using Figma. This hands-on workshop covers wireframing, component libraries, typography hierarchy, and prototyping to set you up for your next design projects.",
            organizerName = "Computer Engineering Council",
            organizerLogo = "https://lh3.googleusercontent.com/aida-public/AB6AXuDBkuM5btIeSGlZYkviOI_ikadaa7meJOX_vVgO0WFCh5PsjNAAqu5bZsfixtExgIjvBFWz_jS7Q67ardG8KKf-FK4oEZEdzW9ClrnnVFFPhgdelnlE8H6Ul2FeMYCWGilxdj2UU7U1Q_kofBpiY28RqlOuM0rdQYKPxOAdpvj6WTx5EZ3MkAFSUAa7NQQrYYwPXPe7eaGw6wA4BL4Sg_phOxO4WChvmlhNA3v6tdEMBq-jlcDdGeE0FQ",
            attendingCount = "24 students are attending",
            registrationStatus = "Open until Oct 12"
        ),
        DetailedEvent(
            id = "2",
            title = "Varsity Basketball: Finals",
            category = "Sports",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBCQTCcNBF1wOpcIMotc_rjOe1Wi_hASDSx7C9F5Qv7Wj5_aaOe9tHGg1hKNDgIi692VsHTgsyqk5uUpN7ehrTVUeCyedT8yHpjf9m3OiaAZAoSRvl0T3WbFF43Tk0q1KTN7Q-jkIAj2FcqhnuAMd4tUPaWX7sXYuMrktXKPc7cvzDO00XE7WWAnyi5EFRXYbdqslFY6DnW41DCgg73eURZpiBGZam_KRFVPh10x28JBy-EY3TbdDNlmg",
            dateTime = "Oct 14 • 6:00 PM",
            location = "Main Sports Complex",
            spotsLeft = null,
            description = "Get ready for the biggest sports event of the semester! Watch the Carolinian Warriors battle it out in the basketball finals. Bring your school spirit, wear blue and gold, and cheer our team to victory!",
            organizerName = "USC Athletics Office",
            organizerLogo = "https://lh3.googleusercontent.com/aida-public/AB6AXuDBkuM5btIeSGlZYkviOI_ikadaa7meJOX_vVgO0WFCh5PsjNAAqu5bZsfixtExgIjvBFWz_jS7Q67ardG8KKf-FK4oEZEdzW9ClrnnVFFPhgdelnlE8H6Ul2FeMYCWGilxdj2UU7U1Q_kofBpiY28RqlOuM0rdQYKPxOAdpvj6WTx5EZ3MkAFSUAa7NQQrYYwPXPe7eaGw6wA4BL4Sg_phOxO4WChvmlhNA3v6tdEMBq-jlcDdGeE0FQ",
            attendingCount = "150 students are attending",
            registrationStatus = "Open until Oct 14"
        ),
        DetailedEvent(
            id = "3",
            title = "International Food Festival",
            category = "Cultural",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDfXIpxvvvQAo3BlRv_6tTRawA7Jqo9rfofDe8cZuV4s4WENfgeuXeEK4TME-P9hMqGIqZtWQwOgcald9_nr2v753ott6XoffJVLaDaLmK_g5VKOFEraYdkwqEhSKnzmvSkKpp0UXRqQa1GTxDGVTodInPhzNKPym0bydsLZj81PYRop5ReUFMFej3ctFu-I_tTNRZmeQtVnIrvqk5llp5AfXfQyIuCZIYFRm1FybIoskqnr3bubaUpPg",
            dateTime = "Oct 15 • 11:00 AM - 3:00 PM",
            location = "Central Campus Green",
            spotsLeft = null,
            description = "Experience cultures and cuisines from around the world! Sample traditional dishes, desserts, and drinks prepared by international students and culinary organizations. Enjoy live cultural performances throughout the day.",
            organizerName = "International Students Association",
            organizerLogo = "https://lh3.googleusercontent.com/aida-public/AB6AXuDBkuM5btIeSGlZYkviOI_ikadaa7meJOX_vVgO0WFCh5PsjNAAqu5bZsfixtExgIjvBFWz_jS7Q67ardG8KKf-FK4oEZEdzW9ClrnnVFFPhgdelnlE8H6Ul2FeMYCWGilxdj2UU7U1Q_kofBpiY28RqlOuM0rdQYKPxOAdpvj6WTx5EZ3MkAFSUAa7NQQrYYwPXPe7eaGw6wA4BL4Sg_phOxO4WChvmlhNA3v6tdEMBq-jlcDdGeE0FQ",
            attendingCount = "85 students are attending",
            registrationStatus = "Open until Oct 15"
        )
    )

    private val defaultEvent = DetailedEvent(
        id = "hackathon",
        title = "University Hackathon 2024",
        category = "Technology",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC-I1nZGdJFlM9v3BflkRsFGD56h_alsClbpZY01mhTE3Cjj6bEdsUx7h39k8fwEN93kXk_qi2KlMh1TLwGW4jsinMr554i8uOJXuY1aYA05BbGph0e3depru9o2uX3HRR425sM2d-H3nt7TQQ6uPJ80B_R7ZnjKbNcZiT01ORBsF59NdBbtPkMLuj2zUsZKNrjFIlSGZeCoED8xtBnLKdBBCvpJa_FsnOY16mJ7uJvGaeuIT27nIUAoA",
        dateTime = "Saturday, Oct 12 • 09:00 AM - 09:00 PM EST",
        location = "Innovation Commons, Building 4, Floor 2 • Campus East",
        spotsLeft = "42 spots left",
        description = "Join us for the annual University Hackathon! Bring your ideas and coding skills to compete for prizes, network with industry professionals, and collaborate with your fellow Carolinians.\n\nWhether you're a seasoned developer or a first-time coder, there's a place for you here. We'll provide the fuel (food and drinks), high-speed internet, and a suite of mentors to help you build the next big thing. Don't miss out on this 12-hour innovation marathon!",
        organizerName = "Computer Engineering Council",
        organizerLogo = "https://lh3.googleusercontent.com/aida-public/AB6AXuDBkuM5btIeSGlZYkviOI_ikadaa7meJOX_vVgO0WFCh5PsjNAAqu5bZsfixtExgIjvBFWz_jS7Q67ardG8KKf-FK4oEZEdzW9ClrnnVFFPhgdelnlE8H6Ul2FeMYCWGilxdj2UU7U1Q_kofBpiY28RqlOuM0rdQYKPxOAdpvj6WTx5EZ3MkAFSUAa7NQQrYYwPXPe7eaGw6wA4BL4Sg_phOxO4WChvmlhNA3v6tdEMBq-jlcDdGeE0FQ",
        attendingCount = "42 students are attending",
        registrationStatus = "Open until Oct 10"
    )

    var event by mutableStateOf<DetailedEvent?>(null)
        private set

    fun loadEvent(id: String) {
        event = detailedEvents.find { it.id == id } ?: defaultEvent
    }
}
