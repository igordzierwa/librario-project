<template>
    <div class="">
        <h2>
            Książki niezarezerwowane
        </h2>
        <el-input
                placeholder="Wyszukaj po tytule"
                v-model="inputText"
                clearable>
        </el-input>
        <el-table
                :data="tableData"
                style="width: 100%"
                id="booksList"
                empty-text="Brak wyników"
        >
            <el-table-column prop="id" label="Identyfikator"></el-table-column>
            <el-table-column prop="name" label="Tytuł"></el-table-column>
            <el-table-column prop="author" label="Autor"></el-table-column>
            <el-table-column prop="length" label="Ilość stron"></el-table-column>
            <el-table-column prop="stockCount" label="Wolne sztuki"></el-table-column>
            <el-table-column
                    fixed="right"
                    label="Akcje"
                    width="120">
                <template slot-scope="scope">
                    <div class="buttons">
                        <el-button @click="handleReservation(scope.row.id)" size="small" type="primary">Rezerwacja</el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog
                    title="Potwierdzenie"
                    :visible.sync="dialogVisible"
                    width="60%"
                    :before-close="handleClose">
                <span>Czy na pewno chcesz zarezerwować książkę: {{toReserve}}?</span>
                <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">Anuluj</el-button>
            <el-button type="primary" @click="handleConfirmReservation">Potwierdzam</el-button>
        </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "AvailableBooks",
        data() {
            return {
                tableData: [],
                inputText: '',
                toReserve: -1,
                dialogVisible: false,
                loggedUser: ''
            }
        },
        methods: {
            loadAvailableBooks: function () {
                this.$axios.get(this.$api + "/rest/books/unreserved", {})
                    .then(resp => {
                        this.tableData = resp.data;
                        console.log(this.tableData);
                    })
                    .catch(resp => {
                        console.log("error", resp);
                    })
            },
            handleReservation: function(variable) {
                console.log(variable);
                this.toReserve = variable;
                this.dialogVisible = true;
            },
            handleConfirmReservation: function () {
                this.dialogVisible = false;

                this.$axios.post(this.$api + "/rest/reservation/" + this.loggedUser +"/" + this.toReserve)
                    .then(resp => {
                        console.log(resp);
                        this.$message({
                            message: 'Poprawnie zarezerwowano ksiażke.',
                            type: 'success'
                        });
                        this.loadAvailableBooks();  //zeby odswiezyla sie lista
                    })
                    .catch(error => {
                        console.log(error);
                        this.$message({
                            message: 'Błąd przy rezerwacji książki.',
                            type: 'error'
                        });
                    });
            }
        },
        beforeUpdate() {
            this.loggedUser = localStorage.getItem('loggedID');
        },
        mounted(){
            this.loadAvailableBooks();
        },
    }
</script>

<style>
    .el-header {
        background-color: #e0e4e9;
        color: #333;
        text-align: center;
        line-height: 60px;
    }
</style>