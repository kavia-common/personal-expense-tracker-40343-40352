androidApplication {
    namespace = "com.smartspender"

    dependencies {
        // Material3 + Compose UI
        implementation("androidx.activity:activity-compose:1.9.3")
        implementation("androidx.compose.ui:ui:1.7.4")
        implementation("androidx.compose.ui:ui-tooling-preview:1.7.4")
        implementation("androidx.compose.material3:material3:1.3.0")
        implementation("androidx.navigation:navigation-compose:2.8.3")

        // Lifecycle & Coroutines
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

        // Room (SQLite) — use runtime/ktx (no kapt in DCL)
        implementation("androidx.room:room-runtime:2.6.1")
        implementation("androidx.room:room-ktx:2.6.1")

        // Icons
        implementation("androidx.compose.material:material-icons-extended:1.7.4")
    }
}
