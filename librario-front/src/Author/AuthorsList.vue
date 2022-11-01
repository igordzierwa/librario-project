<template>
    <div class="">
        <el-table
                :data="tableData"
                style="width: 100%"
                id="authorList"
                empty-text="Brak wyników"
        >
            <el-table-column prop="id" label="Identyfikator"></el-table-column>
            <el-table-column label="Foto" >
                <template slot-scope="scope" >
                    <div class="image-profile">
                    <img v-if="scope.row.profile_photo && scope.row.profile_photo.url" :src="'http:/localhost:8081/resources/gfx/'+scope.row.profile_photo.url"/>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="name" label="Nazwa"></el-table-column>
            <el-table-column prop="year" label="rok urodzenia"></el-table-column>

            <el-table-column
                    fixed="right"
                    label="Akcje"
                    width="120">
                <template slot-scope="scope">
                    <div class="buttons">
                        <router-link tag="el-button" class="myClass" id="button" :to="'/singleauthor/' + scope.row.name">Podląd</router-link>
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
            <span>Czy na pewno chcesz usunąć autora: {{toDeleteId}}?</span>
            <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">Anuluj</el-button>
            <el-button type="primary" @click="handleConfirmDelete">Potwierdzam</el-button>
        </span>
        </el-dialog>
    </div>
</template>
<script>
    export default {
        name: 'authorsList',
        data() {
            return {
                test: 'test',
                loading: true,
                tableData: [],
                dialogVisible: false,
                toDeleteId: -1,
            }
        },
        methods: {
            handleDelete(variable) {
                console.log(variable);
                this.toDeleteId  = variable;
                this.dialogVisible = true;
            },
            handleConfirmDelete() {
                this.dialogVisible = false;

                this.$axios.delete(this.$api + "/rest/author/"+this.toDeleteId)
                    .then(resp => {
                        console.log(resp);
                        this.$message({
                            message: 'Poprawnie usunięto autora.',
                            type: 'success'
                        });
                        this.loadAuthors();  //zeby odswiezyla sie lista
                    })
                    .catch(error => {
                        console.log(error);
                        this.$message({
                            message: 'Błąd przy usuwaniu autora.',
                            type: 'error'
                        });
                    });
            },
            handleEdit(id) {
                //redirect na stronę /authors/id - przekierowanie do komponentu Authors poprzez Router ustawiony w main.js
                this.$router.push("/authors/" + id);
            },
            loadAuthors: function () {
                this.loading = true;

                this.$axios.get(this.$api + "/rest/author", {})
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
            this.loadAuthors()
        }

    }
</script>
<style lang="scss">
    #authorList {
        .buttons {
            button {
                display: block;
                width: 100%;
                margin: 5px auto;
            }
        }
        .image-profile{
            img{
                max-width: 100px;
            }
        }
    }
</style>