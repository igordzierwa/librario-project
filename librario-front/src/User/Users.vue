<template>
    <div class="">
        <el-table
                :data="tableData"
                style="width: 100%"
                id="userList"
                empty-text="Brak wyników"
        >
            <el-table-column prop="username" label="Nazwa Użytkownika"></el-table-column>
            <el-table-column prop="name" label="Imię"></el-table-column>
            <el-table-column prop="surname" label="Nazwisko"></el-table-column>

            <el-table-column
                    fixed="right"
                    label="Akcje"
                    width="150">
                <template slot-scope="scope">
                    <div class="buttons">
                        <router-link tag="el-button" class="myClass" id="button" :to="'/reservations/' + scope.row.username">Rezerwacje</router-link>
                        <el-button @click="handleDelete(scope.row.username)" size="small" type="danger">Usuń</el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog
                title="Potwierdzenie"
                :visible.sync="dialogVisible"
                width="60%"
                :before-close="handleClose">
            <span>Czy na pewno chcesz usunąć pracownika: {{toDeleteUsername}}?</span>
            <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">Anuluj</el-button>
            <el-button type="primary" @click="handleConfirmDelete">Potwierdzam</el-button>
        </span>
        </el-dialog>
    </div>
</template>
<script>
    export default {
        name: 'Employees',
        data() {
            return {
                test: 'test',
                loading: true,
                tableData: [],
                dialogVisible: false,
                toDeleteUsername: "",
            }
        },
        methods: {
            handleDelete(username) {
                console.log(username);
                this.toDeleteUsername  = username;
                this.dialogVisible = true;
            },
            handleConfirmDelete() {
                this.dialogVisible = false;

                this.$axios.delete("http://localhost:8081/admin/users/"+this.toDeleteUsername)
                    .then(resp => {
                        console.log(resp);
                        this.$message({
                            message: 'Poprawnie usunięto uzytkownika.',
                            type: 'success'
                        });
                        this.loadEmployees();  //zeby odswiezyla sie lista
                    })
                    .catch(error => {
                        console.log(error);
                        this.$message({
                            message: 'Błąd przy usuwaniu uzytkownika.',
                            type: 'error'
                        });
                    });
            },
            handleEdit(username) {
                //redirect na stronę /admin/employees - przekierowanie do komponentu addEmployee poprzez Router ustawiony w main.js
                this.$router.push("/users/" + username);
            },
            loadEmployees: function () {
                this.loading = true;

                this.$axios.get("http://localhost:8081/admin/users/", {})
                    .then(resp => {
                        this.tableData = resp.data;
                        console.log(this.tableData);
                        this.loading = false
                    })
                    .catch(resp => {
                        console.log("error", resp);
                        this.loading = false
                    })
            }
        },
        mounted() {
            this.loadEmployees();
            this.loadCounter();
        }

    }
</script>
<style lang="scss">
    #userList {
        .buttons {
            button {
                display: block;
                width: 100%;
                margin: 5px auto;
            }
        }
    }
</style>