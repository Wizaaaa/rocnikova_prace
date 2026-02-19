package com.example.rocnikova_prace.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rocnikova_prace.data.repository.AuthRepository
import com.example.rocnikova_prace.data.repository.QuestionRepository
import com.example.rocnikova_prace.ui.screens.authScreen.AuthViewModel
import com.example.rocnikova_prace.ui.screens.createInformation.CreateInformationViewModel
import com.example.rocnikova_prace.ui.screens.graphScreen.GraphScreenViewModel
import com.example.rocnikova_prace.ui.screens.practiceScreen.PracticeScreenViewModel
import com.example.rocnikova_prace.ui.screens.profileScreen.ProfileScreenViewModel
import com.example.rocnikova_prace.ui.screens.questionsScreen.GroupsViewModel

class GroupIdAndRepositoryFactory(
    private val repository: QuestionRepository,
    private val groupId: String,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PracticeScreenViewModel::class.java) -> {
                PracticeScreenViewModel(repository, groupId) as T
            }
            modelClass.isAssignableFrom(GraphScreenViewModel::class.java) -> {
                GraphScreenViewModel(repository, groupId) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

class GroupIdRepositoryAuthFactory(
    private val repository: QuestionRepository,
    private val authRepository: AuthRepository,
    private val groupId: String,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CreateInformationViewModel::class.java) -> {
                CreateInformationViewModel(repository, authRepository, groupId) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

class QuestionRepositoryFactory(
    private val repository: QuestionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GroupsViewModel::class.java) -> {
                GroupsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileScreenViewModel::class.java) -> {
                ProfileScreenViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

class AuthViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}