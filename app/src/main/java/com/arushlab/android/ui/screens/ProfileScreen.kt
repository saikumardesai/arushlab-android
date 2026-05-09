package com.arushlab.android.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arushlab.android.ui.theme.*
import com.arushlab.android.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var showSignOutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
    ) {
        // ─── Profile Header with gradient ────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(PrimaryRed, PrimaryRedLight))
                )
                .padding(horizontal = 20.dp, vertical = 32.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤", fontSize = 36.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                
                val email = viewModel.currentUserEmail
                val displayName = email.substringBefore("@").replaceFirstChar { it.uppercase() }
                
                Text(displayName, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(email, color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ─── Menu Items ──────────────────────────
        val context = androidx.compose.ui.platform.LocalContext.current
        ProfileMenuItem(Icons.Outlined.Person, "My Profile", "View & edit personal details", onClick = { android.widget.Toast.makeText(context, "Profile editing coming soon", android.widget.Toast.LENGTH_SHORT).show() })
        ProfileMenuItem(Icons.Outlined.DateRange, "My Bookings", "View all your test bookings", onClick = { android.widget.Toast.makeText(context, "Navigating to Bookings...", android.widget.Toast.LENGTH_SHORT).show() })
        ProfileMenuItem(Icons.Outlined.Notifications, "Notifications", "Manage notification preferences", onClick = { android.widget.Toast.makeText(context, "Notification settings coming soon", android.widget.Toast.LENGTH_SHORT).show() })
        ProfileMenuItem(Icons.Outlined.Info, "About ARUSH Lab", "Know more about us", onClick = { android.widget.Toast.makeText(context, "ARUSH Lab v1.0.0", android.widget.Toast.LENGTH_SHORT).show() })
        ProfileMenuItem(Icons.Outlined.Phone, "Contact Support", "Get help via WhatsApp or call", onClick = { android.widget.Toast.makeText(context, "Opening Support...", android.widget.Toast.LENGTH_SHORT).show() })

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), color = Divider)
        Spacer(modifier = Modifier.height(8.dp))

        // Sign out
        Surface(
            onClick = { showSignOutDialog = true },
            modifier = Modifier.fillMaxWidth(),
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.ExitToApp, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Sign Out", color = ErrorRed, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
            }
        }

        // App version
        Text(
            "ARUSH Lab v1.0.0",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            color = TextHint
        )
    }

    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            title = { Text("Sign Out", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to sign out?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.signOut()
                        showSignOutDialog = false
                        onSignOut()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) { Text("Sign Out") }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun ProfileMenuItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(PrimaryRed.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = PrimaryRed, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextHint, modifier = Modifier.size(20.dp))
        }
    }
}
