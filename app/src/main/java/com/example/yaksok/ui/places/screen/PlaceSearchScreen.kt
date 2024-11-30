package com.example.yaksok.ui.places.screen

//@Composable
//fun PlaceSearchScreen(
//    viewModel: PlacesViewModel,
//    onNavigateToDetails: () -> Unit
//) {
//    var searchQuery by remember {
//        mutableStateOf("")
//    }
//    val showDialog by viewModel.showPlaceDialog.collectAsState()
//    val placeSelectionText by viewModel.placeSelectionText.collectAsState()
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        TextField(
//            value = searchQuery,
//            onValueChange = { searchQuery = it },
//            label = { Text("장소 검색") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(
//            onClick = {
//                viewModel.searchPlaces(searchQuery)
//            },
//            modifier = Modifier.align(Alignment.End)
//        ) {
//            Text("검색")
//        }
//    }
//
//    if (showDialog) {
//        PlaceSearchResultsDialog(
//            results = placeSelectionText,
//            onDismiss = { viewModel.closePlaceDialog()},
//            onSelectPlace = { index ->
//                viewModel.selectPlace(index)
//                viewModel.closePlaceDialog()
//                onNavigateToDetails()
//            }
//        )
//    }
//}