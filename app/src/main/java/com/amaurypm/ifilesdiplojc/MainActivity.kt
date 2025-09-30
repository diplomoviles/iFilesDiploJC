package com.amaurypm.ifilesdiplojc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaurypm.ifilesdiplojc.model.Student
import com.amaurypm.ifilesdiplojc.ui.theme.IFilesDiploJCTheme
import com.amaurypm.ifilesdiplojc.ui.theme.SnackbarRed
import org.simpleframework.xml.core.Persister
import java.io.File
import java.io.StringWriter

class MainActivity : ComponentActivity() {

    private val serializer by lazy{
        Persister()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IFilesDiploJCTheme {
                MainScreen(serializer)
            }
        }
    }
}

@Composable
fun MainScreen(
    serializer: Persister
){
    val snackbarHostState = remember { SnackbarHostState() }
    var input by remember { mutableStateOf("") }
    //Para el foco del TextField
    val focusManager = LocalFocusManager.current
    var content by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    Scaffold(
        snackbarHost = { ColoredSnackbarHost(snackbarHostState) },  //SnackbarHost personalizado
        topBar = {
            Image(
                painter = painterResource(id = R.drawable.b_top),
                contentDescription = "Banner superior",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        },
        bottomBar = {
            Image(
                painter = painterResource(id = R.drawable.b_bottom),
                contentDescription = "Banner inferior",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .windowInsetsPadding(
                        WindowInsets.systemBars.only(WindowInsetsSides.Bottom)
                    )
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(32.dp))

            Text(
                text = stringResource(id = R.string.app_name),
                color = Color(0xFF33B4E4),
                fontSize = 60.sp,
                fontFamily = FontFamily(Font(R.font.electrolize)),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                label = { Text("Ingrese texto") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            Spacer(Modifier.height(40.dp))

            // Botón guardar
            Button(
                onClick = {
                    focusManager.clearFocus()

                    if(input.isNotBlank()){
                        try{
                            val file = File(context.filesDir, "app_data.txt")

                            if(!file.exists()) file.createNewFile()

                            val student = Student(name = input)

                            val writer = StringWriter()

                            serializer.write(student, writer)

                            val xmlString = writer.toString()

                            file.writeText(xmlString)

                            input = ""
                            content = ""

                            snackbarHostState.sbMessage(scope, "Información almacenada exitosamente")

                        }
                        catch(e: Exception){
                            e.printStackTrace()
                        }
                    }else{
                        snackbarHostState.sbMessage(scope, "Ingrese la información a almacenar",
                            SnackbarRed
                        )
                    }

                },
                colors =  ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF33B5E5)
                ),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Guardar",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.electrolize)),
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(Modifier.height(32.dp))

            // Contenido
            Text(
                text = content.ifBlank { "" },
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.electrolize)),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(32.dp))

            // Botón leer
            Button(
                onClick = {
                    try{
                        val file = File(context.filesDir, "app_data.txt")
                        if(file.exists()){

                            val xmlString = file.readText()

                            val student = serializer.read(Student::class.java, xmlString)

                            content = "Id: ${student.id}, Nombre: ${student.name}"

                        }else{
                            snackbarHostState.sbMessage(
                                scope,
                                "No existe información o archivo almacenado en el dispositivo",
                                SnackbarRed
                            )
                        }
                    }
                    catch (e: Exception){
                        e.printStackTrace()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0099CC)
                ),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Leer",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.electrolize)),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    IFilesDiploJCTheme {
        MainScreen(Persister())
    }
}