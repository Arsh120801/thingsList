package com.example.shoppinglist


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(val id:Int, var name:String, var quantity: Int, val isEditing: Boolean = false) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp(){

    var idNum = ((100..100000).random()+(100..100000).random())*((100..100000).random()+(100..100000).random())

    var sItems by remember{ mutableStateOf(listOf<ShoppingItem>()) }
    var ShowDialog by remember { mutableStateOf(false) }
    var ItemName by remember { mutableStateOf("") }
    var ItemQuantity by remember { mutableStateOf("") }

    Column(
        modifier= Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { ShowDialog = true},
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        ) {
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItems){
                item->
                if(item.isEditing){
                    ShoppingItemEditor(item = item, OnEditComplete = {
                        editedName, editedQty ->
                        sItems = sItems.map { it.copy(isEditing = false) }
                        val editedItem = sItems.find { it.id == item.id }
                        editedItem ?.let {
                            it.name = editedName
                            it.quantity = editedQty
                        }
                    } )
                }
                else{
                    ShoppingItem(
                        item = item,
                        OnEditClick = {
                            //finding which item we are editing and changing its isEditing boolean
                            sItems = sItems.map { it.copy(isEditing = it.id == item.id) }
                        },
                        OnDeleteClick = {
                            sItems = sItems - item
                        })
                }
            }
        }

        if(ShowDialog){
            AlertDialog(
                onDismissRequest = { ShowDialog = false },
                confirmButton = {
                     Row(
                         modifier = Modifier
                             .fillMaxWidth()
                             .padding(8.dp),
                         horizontalArrangement = Arrangement.SpaceBetween
                     ) {
                         Button(onClick = {
                             if(ItemName !="" && ItemQuantity != ""){
                                 val newItem = ShoppingItem(
                                     id = idNum+1,
                                     name = ItemName,
                                     quantity = ItemQuantity.toInt()
                                 )
                                 sItems = sItems + newItem
                                 ShowDialog = false
                                 ItemName = ""
                                 ItemQuantity=""
                             }
                         }) {
                             Text(text = "Add")
                         }
                         Button(onClick = {
                             ShowDialog = false
                             ItemName = ""
                             ItemQuantity = ""
                         }) {
                             Text(text = "Cancel")
                         }
                     }
                },
                title = { Text(text = "Add Item")},
                text = {
                    Column() {
                        OutlinedTextField(
                            value = ItemName ,
                            onValueChange ={ ItemName=it },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        OutlinedTextField(
                            value = ItemQuantity,
                            onValueChange ={ ItemQuantity=it },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun ShoppingItemEditor(item: ShoppingItem, OnEditComplete: (String,Int) -> Unit){

    var editName by remember{ mutableStateOf(item.name) }
    var editQty by remember{ mutableStateOf(item.quantity.toString()) }
    var isEditing by remember{ mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .border(border = BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(20)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column() {
            BasicTextField(
                value = editName,
                onValueChange = {editName = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editQty,
                onValueChange = {editQty = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }

        Button(modifier=Modifier.padding(12.dp),onClick = {
            if(editName != "" && editQty != ""){
               OnEditComplete(editName, editQty.toIntOrNull() ?: 1 )
            }
            isEditing = false
        }) {
            Text(text = "Save")
        }
    }

}

@Composable
fun ShoppingItem(
    item : ShoppingItem,
    OnEditClick: ()->Unit,
    OnDeleteClick: ()->Unit,
){
    Row (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color.Black),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Text(text = item.name, modifier = Modifier.padding(17.dp))
        Text(text = "Qty: ${item.quantity}", modifier=Modifier.padding(17.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = OnEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = OnDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}