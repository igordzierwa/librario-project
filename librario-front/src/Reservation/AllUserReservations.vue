<template>
    <div>
        <h2>Lista wszystkich rezerwacji</h2>
        <h3>Użytkownik: {{this.username}}</h3>
        <h3>Liczba rezerwacji: {{this.reservationCount}}</h3>
        <el-table
                :data="tableData"
                style="width: 100%"
                id="booksList"
                empty-text="Brak wyników"
        >
            <el-table-column prop="id" label="Identyfikator"></el-table-column>
            <el-table-column prop="bookTitle" label="Tytuł"></el-table-column>
            <el-table-column prop="authorName" label="Autor"></el-table-column>
            <el-table-column prop="reservationDate" label="Data rezerwacji"></el-table-column>
            <el-table-column prop="expiredDate" label="Data wygaśnięcia rezerwacji"></el-table-column>
            <el-table-column
                    fixed="right"
                    label="Akcje"
                    width="120">
                <template slot-scope="scope">
                    <div class="buttons">
                        <el-button @click="handleDeleteReservation(scope.row.id)" size="small" type="danger">Usuń</el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog
                title="Potwierdzenie"
                :visible.sync="dialogVisible"
                width="60%"
                :before-close="handleClose">
            <span>Czy na pewno chcesz usunąć książkę: {{toReserve}}?</span>
            <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">Anuluj</el-button>
            <el-button type="primary" @click="handleConfirmDeleteReservation">Potwierdzam</el-button>
        </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "AllUserReservations",
        data() {
            return {
                tableData: [],
                username: this.$route.params.username,
                dialogVisible: false,
                toDeleteId: -1,
                reservationCount: 0,
            }
        },
        methods: {
            loadReservedBooks: function () {
                this.$axios.get(this.$api + "/rest/reservation/user/" + this.username, {})
                    .then(resp => {
                        this.tableData = resp.data;
                        console.log(this.tableData);
                    })
                    .catch(resp => {
                        console.log("error", resp);
                    })
            },
            handleDeleteReservation: function (variable) {
                console.log(variable);
                this.toDeleteId = variable;
                this.dialogVisible = true;

            },
            handleConfirmDeleteReservation: function () {
                this.dialogVisible = false;

                this.$axios.delete(this.$api + "/rest/reservation/" + this.toDeleteId)
                    .then(resp => {
                        console.log(resp);
                        this.$message({
                            message: 'Poprawnie usunięto rezerwację.',
                            type: 'success'
                        });
                        this.loadReservedBooks();  //zeby odswiezyla sie lista
                    })
                    .catch(error => {
                        console.log(error);
                        this.$message({
                            message: 'Błąd przy usuwaniu rezerwacji.',
                            type: 'error'
                        });
                    });
            },
            loadCounter: function () {
                this.$axios.get(this.$api + "/rest/user/counter/username/" + this.username, {})
                    .then(resp => {
                        this.reservationCount = resp.data;
                        console.log(this.reservationCount)
                    }).catch(resp => {
                    console.log("error", resp);
                })
            }
        },
        mounted() {
            this.loadReservedBooks();
            this.loadCounter();
        }
    }
</script>

<style>
    .el-header {
        background-color: #DCDFE6;
        color: #333;
        text-align: center;
        line-height: 50px;
    }
    .el-main {
        text-align: center;
    }
</style>