<template>
    <div class="">
        <h3>
            Lista wszystkich książek
        </h3>
       <el-input
            placeholder="Wyszukaj po tytule"
            v-model="inputText"
            clearable>
        </el-input>
        <el-table
                :data="filteredTableData"
                style="width: 100%"
                id="booksList"
                empty-text="Brak wyników"
        >
            <el-table-column prop="id" label="Identyfikator"></el-table-column>
            <el-table-column prop="name" label="Tytuł"></el-table-column>
            <el-table-column prop="author" label="Autor"></el-table-column>
            <el-table-column prop="category" label="Kategoria"></el-table-column>
            <el-table-column prop="length" label="Ilość stron"></el-table-column>
            <el-table-column
                    fixed="right"
                    label="Akcje"
                    width="120">
                <template slot-scope="scope">
                    <div class="buttons">
                        <router-link tag="el-button" class="myClass" id="button" :to="'/singlebooks/' + scope.row.id">Podgląd</router-link>
                        <el-button @click="handleDelete(scope.row.id)" size="small" type="danger">Usuń</el-button>
                        <el-button @click="handleEdit(scope.row.id)" size="small" type="primary">Edytuj</el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog
                title="Potwierdzenie"
                :visible.sync="dialogVisible"
                width="60%"
                :before-close="handleClose">
            <span>Czy na pewno chcesz usunąć książkę: {{toDeleteId}}?</span>
            <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">Anuluj</el-button>
            <el-button type="primary" @click="handleConfirmDelete">Potwierdzam</el-button>
        </span>
        </el-dialog>
    </div>
</template>
<script>
    export default {
        name: 'booksList',
        data() {
            return {
                test: 'test',
                loading: true,
                tableData: [],
                inputText: '',
                dialogVisible: false,
                toDeleteId: -1,
            }
        },
        methods: {
            loadBooks: function () {
                this.loading = true;

                this.$axios.get(this.$api + "/rest/books", {})
                    .then(resp => {
                        this.tableData = resp.data;
                        console.log(this.tableData);
                        this.loading = false
                    })
                    .catch(resp => {
                        console.log("error", resp);
                        this.loading = false
                    })
            },
            handleDelete(variable) {
                console.log(variable);
                this.toDeleteId = variable;
                this.dialogVisible = true;
            },
            handleEdit(id){
                //redirect na stronę /authors/id - przekierowanie do komponentu Authors poprzez Router ustawiony w main.js
                this.$router.push("/books/" + id);

            },
            handleClose(){

            },
            handleConfirmDelete() {
                this.dialogVisible = false;

                this.$axios.delete(this.$api + "/rest/books/" + this.toDeleteId)
                    .then(resp => {
                        console.log(resp);
                        this.$message({
                            message: 'Poprawnie usunięto ksiażke.',
                            type: 'success'
                        });
                        this.loadBooks();  //zeby odswiezyla sie lista
                    })
                    .catch(error => {
                        console.log(error);
                        this.$message({
                            message: 'Błąd przy usuwaniu książki.',
                            type: 'error'
                        });
                    });
            },
            //tego jeszcze nie ma
            // handleEdit(id) {
            //     //redirect na stronę /authors/id - przekierowanie do komponentu Authors poprzez Router ustawiony w main.js
            //     this.$router.push("/authors/" + id);
            // }
        },
        computed: {
            filteredTableData: function(){
                return this.tableData.filter((book) => {
                    if (book.name.toLowerCase().match(this.inputText.toLowerCase())) {
                        return book.name;
                    }
                });
            }
        },
         mounted() {
             this.loadBooks()
        }
    }
</script>
<style lang="scss">
    #booksList {
        .buttons {
            button {
                display: block;
                width: 100%;
                margin: 5px auto;
            }
        }
    }
</style>