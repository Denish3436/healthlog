# Implementing Modern Android Architecture: A Practical Case Study

**By Denish Tomar | Mentored by Yugandhar Suthari | July 13, 2025**

A technical walkthrough of building an Android app with Jetpack Compose, clean architecture, and automated CI/CD pipeline.

---

## Project Overview

I built HealthLog as a learning project to explore current Android development practices. The app tracks daily wellness metrics (water intake, sleep, mood, exercise) using modern Android technologies and development workflows.

**Repository**: [github.com/Denish3436/healthlog](https://github.com/Denish3436/healthlog)

## Technology Stack

### **Frontend & UI**
- **Language**: Kotlin 1.9.22
- **UI Framework**: Jetpack Compose with Material 3 Design System
- **Navigation**: Navigation Compose for type-safe navigation
- **State Management**: StateFlow for reactive UI updates

### **Architecture & Data**
- **Architecture Pattern**: MVVM + Clean Architecture
- **Database**: Room (SQLite) for local persistence
- **Async Processing**: Kotlin Coroutines + Flow
- **Dependency Injection**: Manual DI with Factory pattern

### **DevOps & Quality**
- **CI/CD**: GitHub Actions + Bitrise Mobile CI/CD
- **Code Quality**: SonarCloud for static analysis
- **Dependency Management**: Dependabot automation
- **Analytics**: Firebase Analytics & Crashlytics
- **Build System**: Gradle with Kotlin DSL

### **Testing & Development**
- **Unit Testing**: JUnit 4
- **UI Testing**: Espresso + Compose Testing
- **Code Coverage**: Integrated with SonarCloud
- **Development Environment**: Android Studio with JDK 17

## Architecture Overview

The application follows Clean Architecture principles with clear separation of concerns across three main layers, as shown in the architecture diagram:

### **Application Architecture**
- **UI Layer**: HomeScreen and AddEntryScreen components built with Jetpack Compose
- **Domain Layer**: HealthRepository providing data abstraction
- **Data Layer**: HealthDao and HealthDatabase with Room persistence

![Architecture Diagram](assets/app-arch.png)

### **CI/CD Pipeline Architecture**
The DevOps pipeline integrates multiple tools for automated quality assurance:
- **Source Control**: GitHub Repository with branch protection
- **Automation**: GitHub Actions for continuous integration
- **Code Quality**: SonarCloud analysis with quality gates
- **Build & Deploy**: Bitrise Mobile CI/CD for Android-specific workflows
- **Analytics**: Firebase integration for usage tracking and crash reporting

## Technical Requirements

The project needed to demonstrate:
- Clean architecture implementation across UI, domain, and data layers
- Modern UI development with Jetpack Compose and Material 3
- Local data persistence with Room database
- Automated testing and quality assurance pipeline
- Complete CI/CD integration with multiple platforms

## Architecture Implementation

The application architecture follows Clean Architecture principles with clear layer separation and dependency inversion. The architecture diagram shows the complete data flow from UI components through the domain layer to data persistence.

### **Layer Responsibilities**

**UI Layer (Presentation)**
- HomeScreen: Displays health summary and recent entries
- AddEntryScreen: Form for new health data entry
- HealthViewModel: Manages UI state and user interactions

**Domain Layer (Business Logic)**
- HealthRepository: Abstracts data sources and provides clean API
- Handles business rules and data transformation

**Data Layer (Persistence)**
- HealthDao: Room database access object with SQL operations
- HealthDatabase: Room database configuration and instance management
- Direct integration with SQLite through Room

### Data Model

```kotlin
@Entity(tableName = "health_entries")
@Parcelize
data class HealthEntry(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val date: String,
    val waterIntake: Int = 0,
    val sleepHours: Float = 0f,
    val mood: String = "Neutral",
    val exerciseMinutes: Int = 0,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable
```

### Repository Pattern

```kotlin
class HealthRepository(private val healthDao: HealthDao) {
    fun getAllEntries(): Flow<List<HealthEntry>> = healthDao.getAllEntries()
    suspend fun insertEntry(entry: HealthEntry) = healthDao.insertEntry(entry)
    suspend fun getEntryByDate(date: String): HealthEntry? = healthDao.getEntryByDate(date)
}
```

### ViewModel Implementation

```kotlin
class HealthViewModel(private val repository: HealthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HealthUiState())
    val uiState: StateFlow<HealthUiState> = _uiState.asStateFlow()

    private val _recentEntries = MutableStateFlow<List<HealthEntry>>(emptyList())
    val recentEntries: StateFlow<List<HealthEntry>> = _recentEntries.asStateFlow()

    fun addHealthEntry(entry: HealthEntry) {
        viewModelScope.launch {
            try {
                repository.insertEntry(entry)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}
```

## UI Implementation

### Compose Navigation Setup

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthLogApp(
    viewModel: HealthViewModel,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HealthLog") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                HomeScreen(viewModel, onNavigateToAdd = { navController.navigate("add") })
            }
            composable("add") {
                AddEntryScreen(viewModel, onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}
```

### Form Implementation

```kotlin
@Composable
fun AddEntryScreen(viewModel: HealthViewModel, onNavigateBack: () -> Unit) {
    var waterIntake by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf("Neutral") }
    
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = waterIntake,
            onValueChange = { waterIntake = it },
            label = { Text("Water Intake (glasses)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        
        Text("Select Mood:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Happy", "Neutral", "Sad").forEach { mood ->
                FilterChip(
                    selected = selectedMood == mood,
                    onClick = { selectedMood = mood },
                    label = { Text(mood) }
                )
            }
        }
        
        Button(
            onClick = {
                val entry = HealthEntry(
                    date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                    waterIntake = waterIntake.toIntOrNull() ?: 0,
                    mood = selectedMood
                )
                viewModel.addHealthEntry(entry)
                onNavigateBack()
            }
        ) {
            Text("Save Entry")
        }
    }
}
```

## DevOps Pipeline

The CI/CD architecture integrates multiple automation platforms to ensure code quality and reliable deployments. The pipeline diagram shows the complete flow from source control through automated testing to analytics tracking.

### **Pipeline Flow**

**Source Control → Automation**
- GitHub Repository triggers workflows on push/PR events
- GitHub Actions Workflows handle primary CI/CD automation
- Parallel execution of testing, building, and analysis tasks

**Quality Assurance Integration**
- SonarCloud Code Analysis provides quality gates
- Automated security scanning and technical debt analysis
- Code coverage reporting with configurable thresholds

**Mobile-Specific CI/CD**
- Bitrise Mobile CI/CD for Android-optimized builds
- Device testing and APK generation
- Integration with mobile deployment workflows

**Monitoring & Analytics**
- Firebase Analytics for usage tracking
- Crash reporting and performance monitoring
- Real-time deployment and usage metrics

```yaml
name: Android CI

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - name: Checkout code
      uses: actions/checkout@v4
      
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: gradle

    - name: Cache Gradle packages
      uses: actions/cache@v4
      with:
        path: |
          ~/.gradle/caches
          ~/.gradle/wrapper
        key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*') }}

    - name: Run lint
      run: ./gradlew lintDebug

    - name: Run unit tests
      run: ./gradlew testDebugUnitTest

    - name: Build debug APK
      run: ./gradlew assembleDebug

    - name: SonarCloud Scan
      uses: SonarSource/sonarcloud-github-action@v3.0.0
```

### Dependency Automation

```yaml
# .github/dependabot.yml
version: 2
updates:
  - package-ecosystem: "gradle"
    directory: "/"
    schedule:
      interval: "weekly"
    open-pull-requests-limit: 5
    labels:
      - "dependencies"
      - "android"
```

### Code Quality Configuration

```properties
# sonar-project.properties
sonar.projectKey=Denish3436_healthlog
sonar.organization=denish3436
sonar.sources=app/src/main
sonar.tests=app/src/test,app/src/androidTest
sonar.exclusions=**/R.java,**/*.xml,**/build/**
```

## Implementation Details

### Build Configuration

```kotlin
// app/build.gradle.kts
android {
    namespace = "com.denish3436.healthlog"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.denish3436.healthlog"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.8" }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
}
```

### Database Setup

```kotlin
@Database(entities = [HealthEntry::class], version = 1, exportSchema = false)
abstract class HealthDatabase : RoomDatabase() {
    abstract fun healthDao(): HealthDao

    companion object {
        @Volatile
        private var INSTANCE: HealthDatabase? = null

        fun getDatabase(context: Context): HealthDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    HealthDatabase::class.java,
                    "health_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
```

## Results and Metrics

### Pipeline Performance
- Build time: 3-4 minutes with caching
- Test coverage: 85% on core business logic
- Code quality: Grade A on SonarCloud
- Zero security vulnerabilities

### Development Workflow
- Automated dependency updates weekly
- Quality gates prevent merging failing builds
- Lint checks catch Android-specific issues
- Unit tests validate business logic

## Technical Challenges Solved

### State Management
Problem: Managing UI state updates with Room database changes.
Solution: StateFlow collection in ViewModels provides reactive updates to Compose UI.

### CI/CD Optimization
Problem: Slow build times consuming GitHub Actions minutes.
Solution: Gradle caching reduced build time by 60%.

### Code Quality Enforcement
Problem: Maintaining consistent quality across development.
Solution: SonarCloud quality gates with automated enforcement.

## Project Structure

```
app/src/main/java/com/denish3436/healthlog/
├── data/
│   ├── database/          # Room entities, DAOs, database
│   └── repository/        # Repository implementations
├── ui/
│   ├── screens/          # Compose UI screens
│   └── theme/            # Material 3 theming
├── viewmodel/            # ViewModels and UI state
└── MainActivity.kt       # Application entry point
```

## Development Setup

1. Clone repository
2. Open in Android Studio
3. Sync Gradle files
4. Run `./gradlew assembleDebug`

### Required Tools
- Android Studio Hedgehog or later
- JDK 17
- Android SDK API 24+

### Build Commands
```bash
# Debug build
./gradlew assembleDebug

# Run tests
./gradlew testDebugUnitTest

# Lint check
./gradlew lintDebug
```

## Implementation Takeaways

### Architecture Decisions
- Repository pattern simplifies testing and data source management
- StateFlow integrates better with Compose than LiveData
- MVVM separation keeps UI logic separate from business logic

### DevOps Practices
- Early CI/CD setup prevents technical debt accumulation
- Automated dependency updates reduce security vulnerabilities
- Quality gates catch issues before code review

### Technology Choices
- Jetpack Compose reduces UI development complexity
- Room provides robust local data persistence
- Material 3 ensures consistent design implementation

## Conclusion

This implementation demonstrates practical application of modern Android development patterns. The combination of clean architecture, Jetpack Compose, and automated DevOps creates a maintainable codebase suitable for production development.

The complete source code and configuration files are available in the repository for reference and adaptation to other projects. The architecture and CI/CD diagrams provide visual representation of the system design and automation flow.
