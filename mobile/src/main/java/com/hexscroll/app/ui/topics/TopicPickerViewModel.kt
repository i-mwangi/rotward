package com.hexscroll.app.ui.topics
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexscroll.app.core.model.LearningTopic
import com.hexscroll.app.core.repository.UserSettingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class TopicPickerViewModel @Inject constructor(private val store: UserSettingsStore) : ViewModel() {
    private val _selected = MutableStateFlow<Set<LearningTopic>>(emptySet())
    val selected: StateFlow<Set<LearningTopic>> = _selected.asStateFlow()
    val allTopics = LearningTopic.entries
    init { viewModelScope.launch { _selected.value = store.getSelectedTopics() } }
    fun toggle(topic: LearningTopic) {
        _selected.update { current ->
            if (current.contains(topic)) {
                current - topic
            } else if (current.size < LearningTopic.MAX_SELECTION) {
                current + topic
            } else current
        }
    }
    fun canAddMore() = _selected.value.size < LearningTopic.MAX_SELECTION
    fun isValid() = _selected.value.size >= LearningTopic.MIN_SELECTION
    fun confirm(onDone: () -> Unit) {
        viewModelScope.launch {
            store.setSelectedTopics(_selected.value)
            store.setHasCompletedTopicOnboarding(true)
            onDone()
        }
    }
}