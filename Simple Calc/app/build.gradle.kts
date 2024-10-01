
plugins {
    id("com.android.application")
}

android {
    namespace = "edu.sjsu.android.placeholdername"
    compileSdk = 34

    defaultConfig {
        applicationId = "edu.sjsu.android.placeholdername"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packaging {
        resources {
            excludes.add("META-INF/DEPENDENCIES")
            pickFirsts.add("**/*.xsd")
            pickFirsts.add("META-INF/*.md")
            pickFirsts.add("**/LICENSE.txt")
            pickFirsts.add("**/Messages.properties")
            pickFirsts.add("**/Log4j2Plugins.dat")
        }
    }
}

dependencies {
    implementation("com.github.gregcockroft:AndroidMath:ALPHA")
    implementation("com.github.FasterXML:jackson-core:jackson-core-2.14.3")
    implementation("org.matheclipse:matheclipse-core:3.0.0") {
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-core")
    }
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    runtimeOnly("com.celeral:log4j2-android:1.0.0")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("org.mariuszgromada.math:MathParser.org-mXparser:5.2.1")
}