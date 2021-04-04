package com.example.captureinvoice

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.captureinvoice.ui.theme.CaptureInvoiceTheme

class MainActivity : ComponentActivity() {
    private val medicineViewModel: MedicineViewModel by viewModels {
        MedicineViewModelFactory((application as CapturInvoiceApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaptureInvoiceTheme {
                CaptureInvoiceNav(medicineViewModel)
            }
        }
    }
}

@Composable
fun CaptureInvoiceNav(medicineViewModel: MedicineViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        // TODO: change back to welcome
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeScreen(navController = navController)
        }
        composable("new_order") {
            NewOrderScreen(navController = navController, medicineViewModel = medicineViewModel)
        }
        composable("new_medicine") {
            NewMedicineScreen(navController = navController, medicineViewModel = medicineViewModel)
        }
        composable("search_medicine") {
            SearchMedicineScreen(
                navController = navController,
                medicineViewModel = medicineViewModel
            )
        }
    }
}


@Composable
fun WelcomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Welcome")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navController.navigate("new_order") }) {
                Text("Start prototype")
            }
        }
    }
}

@Composable
fun NewOrderScreen(navController: NavController, medicineViewModel: MedicineViewModel) {

    val invoiceNr: String by medicineViewModel.invoiceNr.observeAsState("")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("New order")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            LabelText(text = "Invoice Nr.")
            TextField(
                value = invoiceNr,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { medicineViewModel.onInvoiceNrChange(it) })


            Spacer(modifier = Modifier.height(32.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Medicines", modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .weight(1f), style = MaterialTheme.typography.h6
                )
                Button(
                    onClick = { navController.navigate("new_medicine") },
                    Modifier
                        .wrapContentWidth(Alignment.End)
                        .weight(1f)
                ) {
                    Text("Add medicine")
                }
            }

            Divider(
                color = Color.LightGray, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(medicineViewModel.newMedicines) { newMedicine ->
                    NewMedicineItem(newMedicine)
                }
            }
        }
    }
}

@Composable
fun NewMedicineItem(name: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(
            name,
            Modifier
                .wrapContentWidth(Alignment.Start)
                .weight(1f)
        )
        IconButton(
            onClick = {},
            Modifier
                .wrapContentWidth(Alignment.End)
                .weight(1f)
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit")
        }
    }
}

@Preview
@Composable
fun NewMedicineItemPreview() {
    NewMedicineItem("hello bob")
}

@Composable
fun NewMedicineScreen(navController: NavController, medicineViewModel: MedicineViewModel) {
    val (batchNr, setBatchNr) = remember { mutableStateOf(" ") }
    val qty = remember { mutableStateOf(0f) }
    val packNr = remember { mutableStateOf(0f) }
    val selectedMedicine = medicineViewModel.selectedMedicine.observeAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    fun onSaveClicked() {
        if(selectedMedicine.value.isNullOrEmpty()) {
            Toast.makeText(context, "Select medicine first", Toast.LENGTH_SHORT).show()
        } else {
            medicineViewModel.newMedicines.add(selectedMedicine.value.toString())
            medicineViewModel.onMedicineChange("")
            navController.navigate("new_order")
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Add new medicine")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxWidth().verticalScroll(scrollState)
        ) {

            LabelText(text = "Medicine name")
            OutlinedButton(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.Start),
                onClick = { navController.navigate("search_medicine") }) {
                if (selectedMedicine.value.isNullOrEmpty()) {
                    Text("Select Medicine")
                } else {
                    Text(selectedMedicine.value.toString())
                }
            }


            LabelText(text = "Batch Nr.")
            TextField(
                value = batchNr,
                onValueChange = setBatchNr,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )

            LabelText(text = "Expiry month")
            ChipGroup(
                listOf(
                    "Jan",
                    "Feb",
                    "Mar",
                    "Apr",
                    "May",
                    "Jun",
                    "July",
                    "Aug",
                    "Sep",
                    "Oct",
                    "Nov",
                    "Dec"
                )
            )

            LabelText(text = "Expiry year")
            ChipGroup(listOf("2021", "2022", "2023", "2024", "2025", "2026"))

            LabelText(text = "Qty: ${(qty.value * 20).toInt()}")
            Slider(value = qty.value, onValueChange = { qty.value = it })

            LabelText(text = "Pack")
            ChipGroup(listOf("Tablets", "Bottles", "Tube", "Packet"))

            LabelText("Qty in pack: ${(packNr.value * 20).toInt()}")
            Slider(value = packNr.value, onValueChange = { packNr.value = it })

            Spacer(Modifier.height(8.dp))

            Button(modifier = Modifier.align(Alignment.End),
                onClick = { onSaveClicked()}) {
                Text("Save medicine")
            }

            Spacer(Modifier.height(32.dp))


        }
    }
}

@Composable
fun SearchMedicineScreen(navController: NavController, medicineViewModel: MedicineViewModel) {

    val searchQuery: String by medicineViewModel.searchQuery.observeAsState("")
    val lazyPagingItems = medicineViewModel.medicineFlow.collectAsLazyPagingItems()
    val selectedMedicine = medicineViewModel.selectedMedicine.observeAsState(initial = "")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Select medicine")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 4.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
        ) {
            TextField(
                value = searchQuery,
                placeholder = { Text("Search...") },
                onValueChange = { medicineViewModel.onSearchQueryChange(it) },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(lazyPagingItems) { medicine ->

                    if (medicine != null) {
                        MedicineListItem(medicine.string,
                            isSelected = medicine.string == selectedMedicine.value,
                            onClick = { medicineViewModel.onMedicineChange(it) }
                        )
                    } else {
                        Text(text = "loading...")
                    }
                }
            }
        }
    }
}

@Composable
fun MedicineListItem(
    medicine: String = "",
    isSelected: Boolean = false,
    onClick: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick(medicine) }
    ) {
        Text(
            medicine, modifier = Modifier
                .padding(start = 4.dp)
                .weight(1f)
                .wrapContentWidth(Alignment.Start)
        )
        if (isSelected) {
            Icon(
                Icons.Filled.Check, contentDescription = "Check",
                modifier = Modifier
                    .padding(end = 4.dp)
                    .weight(1f)
                    .wrapContentWidth(
                        Alignment.End
                    )
            )
        }
    }
}


@Composable
fun LabelText(text: String) {
    Text(
        text,
        Modifier.padding(top = 24.dp, bottom = 4.dp),
        style = MaterialTheme.typography.caption
    )
}

@Composable
fun ChipGroup(items: List<String>) {
    val selected = remember { mutableStateOf(items.first()) }
    val scrollState = rememberScrollState()
    Row(Modifier.horizontalScroll(scrollState)) {
        for (item in items) {
            Chip(item, item == selected.value, { selected.value = it })
        }
    }
}

@Composable
fun Chip(
    name: String,
    isSelected: Boolean,
    onCategoryClick: (String) -> Unit,
) {
    Surface(
        modifier = Modifier
            .padding(end = 16.dp)
            .height(50.dp)
            .clickable { onCategoryClick(name) },
        shape = MaterialTheme.shapes.small,
        color = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(name)
            if (isSelected) {
                Spacer(Modifier.width(4.dp))
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "Check"
                )
            }
        }

    }
}

@Deprecated(message = "we might use the chip groups instead")
@Composable
fun MonthPicker() {
    var selectedIndex by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    val items =
        listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Sep", "Oct", "Nov", "Dec")

    Row(
        Modifier
            .padding(8.dp)
            .background(Color.LightGray)
    ) {
        Text(
            items[selectedIndex], modifier = Modifier
                .padding(8.dp)
                .clickable(onClick = { expanded = true })
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                }) {
                    Text(s)
                }
            }
        }
    }
}




