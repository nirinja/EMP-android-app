package com.example.recipegridapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipegridapp.ui.theme.RecipeGridAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeGridAppTheme {
                val navController = rememberNavController()
                RecipeApp(navController)
            }
        }
    }
}

@Composable
fun RecipeApp(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "receptiGrid") {
        composable("receptiGrid") {
            ReceptiGrid(navController = navController)
        }
        composable("recipeDetail/{recipeName}/{imageRes}") { backStackEntry ->
            val imeRecepta = backStackEntry.arguments?.getString("imeRecepta") ?: "Neznan recept"
            val imageRes = backStackEntry.arguments?.getString("imageRes")?.toInt() ?: R.drawable.ic_launcher_foreground
            ReceptVsakPosebaj(imeRecepta = imeRecepta, imageRes = imageRes)
        }
    }
}

@Composable
fun ReceptiGrid(navController: NavHostController) {
    val recepti = listOf(
        "Testenine Bolognese" to R.drawable.pasta_bolognese,
        "Pica z gobicami" to R.drawable.pizza_gobice,
        "Cimetove pletenice" to R.drawable.cimetove_pletenice,
        "Pečena raca v pečici" to R.drawable.pecena_raca_v_pecici,
        "Sirov kruh v plasteh" to R.drawable.sirov_kruh_v_plasteh,
        "Jajčna solata s skuto" to R.drawable.jajcna_solata_s_skuto,
        "Mesna pita s krompirjem" to R.drawable.mesna_pita_s_krompirjem,
        "Miške z jogurtom in jabolki" to R.drawable.miske_z_jogurtom_in_jabolki,
        "Orehovo limonine rezine" to R.drawable.orehovo_limonine_rezine
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(recepti.size) { index ->
            val (ime, imageRes) = recepti[index]
            ReceptKartica(
                imeRecepta = ime,
                imageRes = imageRes,
                onClick = {
                    navController.navigate("recipeDetail/$ime/$imageRes")
                }
            )
        }
    }
}

@Composable
fun ReceptKartica(imeRecepta: String, imageRes: Int, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = imeRecepta,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = imeRecepta,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ReceptVsakPosebaj(imeRecepta: String, imageRes: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = imeRecepta,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = imeRecepta,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "To je podroben opis za $imeRecepta. Tukaj lahko vključite sestavine, navodila in več.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}