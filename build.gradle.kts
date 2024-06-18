
val libraryGroup by rootProject.extra {
    "com.ydslib.widget"
}
val vCode by rootProject.extra {
    100
}
val vName by rootProject.extra {
    "1.0.0"
}

plugins {
    id("com.android.application") version "7.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    id("com.android.library") version "7.4.2" apply false
}