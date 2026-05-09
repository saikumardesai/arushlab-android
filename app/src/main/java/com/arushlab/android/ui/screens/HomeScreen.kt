package com.arushlab.android.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arushlab.android.model.TestModel
import com.arushlab.android.ui.theme.*
import com.arushlab.android.viewmodel.HomeViewModel
import com.arushlab.android.viewmodel.UiState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToBookTest: () -> Unit,
    onNavigateToTracking: (String?) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .statusBarsPadding()
    ) {
        // ─── Top Header ─────────────────────────────────
        HomeTopBar(onNavigateToTracking)

        // ─── Search Bar ─────────────────────────────────
        HomeSearchBar(onClick = onNavigateToBookTest)

        // ─── Banner Carousel ────────────────────────────
        PromoBannerCarousel()

        Spacer(modifier = Modifier.height(20.dp))

        // ─── Quick Actions Grid ─────────────────────────
        QuickActionsGrid(onNavigateToBookTest, onNavigateToTracking)

        Spacer(modifier = Modifier.height(24.dp))

        // ─── Popular Tests ──────────────────────────────
        SectionHeader(title = "Popular Tests", subtitle = "Most booked diagnostics", onSeeAll = onNavigateToBookTest)
        Spacer(modifier = Modifier.height(12.dp))
        PopularTestsRow(uiState, onNavigateToBookTest)

        Spacer(modifier = Modifier.height(24.dp))

        // ─── Health Packages ────────────────────────────
        SectionHeader(title = "Health Packages", subtitle = "Comprehensive checkups")
        Spacer(modifier = Modifier.height(12.dp))
        HealthPackagesRow()

        Spacer(modifier = Modifier.height(24.dp))

        // ─── Why Choose Us ──────────────────────────────
        WhyChooseUsCards()

        Spacer(modifier = Modifier.height(24.dp))

        // ─── Contact Banner ─────────────────────────────
        ContactBanner()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ══════════════════════════════════════════════════════════════
//  TOP BAR
// ══════════════════════════════════════════════════════════════
@Composable
private fun HomeTopBar(onNavigateToTracking: (String?) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "ARUSH Lab",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryRed
                )
            )
            Text(
                text = "& Diagnostics",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
            )
        }
        // Notification bell
        IconButton(onClick = { }) {
            BadgedBox(badge = {
                Badge(containerColor = PrimaryRed) { Text("2", color = Color.White, fontSize = 9.sp) }
            }) {
                Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", tint = TextPrimary)
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  SEARCH BAR (clickable → navigates to Book Test)
// ══════════════════════════════════════════════════════════════
@Composable
private fun HomeSearchBar(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
        color = SurfaceLight,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = TextHint, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Search tests, packages, health checkups...", color = TextHint, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  PROMO BANNER CAROUSEL
// ══════════════════════════════════════════════════════════════
@Composable
private fun PromoBannerCarousel() {
    val banners = listOf(
        BannerData("🏥", "Full Body Checkup", "Flat 40% OFF", "60+ Tests Included", listOf(GradientStart, Color(0xFFFF6B6B))),
        BannerData("🩸", "Blood Test at Home", "Starting ₹199", "Free Home Collection", listOf(Color(0xFF6C63FF), Color(0xFF9C88FF))),
        BannerData("💊", "Diabetes Package", "Save ₹500", "HbA1c + Glucose + More", listOf(Color(0xFF00B4D8), Color(0xFF48CAE4)))
    )
    var currentPage by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3500)
            currentPage = (currentPage + 1) % banners.size
        }
    }

    Column {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 12.dp)
        ) {
            items(banners) { banner ->
                BannerCard(banner)
            }
        }
        // Dot indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            banners.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (index == currentPage) 20.dp else 6.dp, 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (index == currentPage) PrimaryRed else PrimaryRed.copy(alpha = 0.2f))
                )
            }
        }
    }
}

data class BannerData(val emoji: String, val title: String, val offer: String, val subtitle: String, val gradient: List<Color>)

@Composable
private fun BannerCard(banner: BannerData) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(Dimens.BannerHeight),
        shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
        elevation = CardDefaults.cardElevation(Dimens.ElevationMedium)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(banner.gradient))
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(banner.emoji, fontSize = 32.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(banner.title, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(banner.offer, color = Color.White.copy(alpha = 0.95f), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(banner.subtitle, color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  QUICK ACTIONS GRID
// ══════════════════════════════════════════════════════════════
@Composable
private fun QuickActionsGrid(onBookTest: () -> Unit, onTrack: (String?) -> Unit) {
    val actions = listOf(
        QuickAction("🩸", "Blood\nTest", CategoryBlood),
        QuickAction("🧬", "Hormone\nTest", CategoryHormone),
        QuickAction("💉", "Diabetes\nPanel", CategoryDiabetes),
        QuickAction("🦋", "Thyroid\nProfile", CategoryThyroid),
        QuickAction("💊", "Vitamin\nTest", CategoryVitamin),
        QuickAction("❤️", "Full Body\nCheckup", CategoryHealth),
        QuickAction("🏥", "Liver\nFunction", CategoryLiver),
        QuickAction("📋", "Track\nReport", CategoryKidney)
    )

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text("Quick Actions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // 2 rows of 4
        for (row in actions.chunked(4)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                row.forEach { action ->
                    QuickActionItem(
                        action = action,
                        onClick = {
                            if (action.label.contains("Track")) onTrack(null) else onBookTest()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

data class QuickAction(val emoji: String, val label: String, val color: Color)

@Composable
private fun QuickActionItem(action: QuickAction, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(Dimens.CornerRadiusMedium))
            .clickable(onClick = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(Dimens.CornerRadiusMedium))
                .background(action.color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(action.emoji, fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            action.label,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            lineHeight = 14.sp,
            maxLines = 2
        )
    }
}

// ══════════════════════════════════════════════════════════════
//  SECTION HEADER
// ══════════════════════════════════════════════════════════════
@Composable
private fun SectionHeader(title: String, subtitle: String? = null, onSeeAll: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            subtitle?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
        onSeeAll?.let {
            TextButton(onClick = it) {
                Text("See All", color = PrimaryRed, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.width(2.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = PrimaryRed, modifier = Modifier.size(16.dp))
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  POPULAR TESTS HORIZONTAL ROW
// ══════════════════════════════════════════════════════════════
@Composable
private fun PopularTestsRow(uiState: UiState<List<TestModel>>, onBookTest: () -> Unit) {
    when (uiState) {
        is UiState.Loading -> {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(3) { ShimmerTestCard() }
            }
        }
        is UiState.Success -> {
            val tests = uiState.data.take(6)
            if (tests.isEmpty()) {
                EmptyTestsPlaceholder(onBookTest)
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tests) { test -> PopularTestCard(test, onBookTest) }
                }
            }
        }
        is UiState.Error -> {
            EmptyTestsPlaceholder(onBookTest)
        }
    }
}

@Composable
private fun PopularTestCard(test: TestModel, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(170.dp),
        shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(Dimens.ElevationSmall)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                test.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "₹${test.price.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryRed
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.Add, contentDescription = "Book", tint = PrimaryRed, modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(PrimaryRed.copy(alpha = 0.1f))
                    .padding(4.dp))
            }
        }
    }
}

@Composable
private fun ShimmerTestCard() {
    Card(
        modifier = Modifier
            .width(170.dp)
            .height(140.dp),
        shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = Shimmer.copy(alpha = 0.3f))
    ) {}
}

@Composable
private fun EmptyTestsPlaceholder(onBookTest: () -> Unit) {
    Card(
        onClick = onBookTest,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = PrimaryRed.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🔬", fontSize = 36.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Browse All Tests", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text("Tap to explore our diagnostic catalogue", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  HEALTH PACKAGES ROW
// ══════════════════════════════════════════════════════════════
@Composable
private fun HealthPackagesRow() {
    val packages = listOf(
        HealthPkg("Basic Health", "30+ Tests", "₹999", "₹1,999", listOf(Color(0xFF667EEA), Color(0xFF764BA2))),
        HealthPkg("Advanced Health", "60+ Tests", "₹1,999", "₹3,999", listOf(Color(0xFFF093FB), Color(0xFFF5576C))),
        HealthPkg("Master Health", "80+ Tests", "₹2,999", "₹5,999", listOf(Color(0xFF4FACFE), Color(0xFF00F2FE)))
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(packages) { pkg -> HealthPackageCard(pkg) }
    }
}

data class HealthPkg(val name: String, val tests: String, val price: String, val mrp: String, val gradient: List<Color>)

@Composable
private fun HealthPackageCard(pkg: HealthPkg) {
    Card(
        modifier = Modifier.width(220.dp),
        shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
        elevation = CardDefaults.cardElevation(Dimens.ElevationMedium)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(pkg.gradient))
                .padding(16.dp)
        ) {
            Column {
                Surface(color = Color.White.copy(alpha = 0.25f), shape = RoundedCornerShape(6.dp)) {
                    Text(
                        "POPULAR",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(pkg.name, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(pkg.tests, color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(pkg.price, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        pkg.mrp,
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall.copy(
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                        )
                    )
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  WHY CHOOSE US
// ══════════════════════════════════════════════════════════════
@Composable
private fun WhyChooseUsCards() {
    val features = listOf(
        Feature(Icons.Outlined.CheckCircle, Color(0xFF2E7D32), Color(0xFFE8F5E9), "NABL Certified", "Highest accuracy standards"),
        Feature(Icons.Outlined.Home, Color(0xFF1565C0), Color(0xFFE3F2FD), "Home Collection", "Expert technicians at your door"),
        Feature(Icons.Outlined.Notifications, Color(0xFFE65100), Color(0xFFFFF3E0), "Fast Reports", "Digital reports in 6-24 hrs"),
        Feature(Icons.Outlined.Lock, Color(0xFF6A1B9A), Color(0xFFF3E5F5), "100% Secure", "Confidential data handling")
    )
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text("Why Choose ARUSH Lab", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            features.take(2).forEach { f -> FeatureCard(f, Modifier.weight(1f)) }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            features.drop(2).forEach { f -> FeatureCard(f, Modifier.weight(1f)) }
        }
    }
}

data class Feature(val icon: ImageVector, val iconTint: Color, val bgColor: Color, val title: String, val desc: String)

@Composable
private fun FeatureCard(f: Feature, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(f.bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(f.icon, contentDescription = null, tint = f.iconTint, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(f.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(f.desc, style = MaterialTheme.typography.bodySmall, color = TextSecondary, lineHeight = 16.sp)
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  CONTACT BANNER
// ══════════════════════════════════════════════════════════════
@Composable
private fun ContactBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = PrimaryRed)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Need Help?", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Call us anytime for assistance", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text("📞 +91 9482724054", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            }
            Icon(Icons.Default.Phone, contentDescription = null, tint = Color.White, modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
                .padding(10.dp))
        }
    }
}
