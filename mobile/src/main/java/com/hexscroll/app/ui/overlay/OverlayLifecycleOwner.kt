package com.hexscroll.app.ui.overlay
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
class OverlayLifecycleOwner : LifecycleOwner, SavedStateRegistryOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry
    fun attachToView(view: View) {
        savedStateRegistryController.performAttach()
        savedStateRegistryController.performRestore(null)
        view.setViewTreeLifecycleOwner(this)
        view.setViewTreeSavedStateRegistryOwner(this)
    }
    fun onCreate() { lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE) }
    fun onStart()  { lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START) }
    fun onResume() { lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME) }
    fun onPause()  { lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE) }
    fun onStop()   { lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP) }
    fun onDestroy(){ lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY) }
}