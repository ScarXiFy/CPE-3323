package com.usc.cems.data.repository

import com.usc.cems.data.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor() : EventRepository {

    private val _events = MutableStateFlow<List<Event>>(
        listOf(
            Event(
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
            Event(
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
            Event(
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
    )

    override fun getEvents(): StateFlow<List<Event>> {
        return _events.asStateFlow()
    }

    override fun getEventById(id: String): Event? {
        return _events.value.find { it.id == id }
    }

    override suspend fun addEvent(event: Event): Result<Unit> = runCatching {
        val currentList = _events.value.toMutableList()
        currentList.add(0, event) // Insert at the top of the feed
        _events.value = currentList
    }
}
