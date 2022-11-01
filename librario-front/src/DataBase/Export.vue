<template>
    <div id="export">

        <h3>
            Export bazy książek
        </h3>

        <el-form>
            <el-row>
                <el-form ref="Export">
                    <el-button type="success" @click="handleExport">Eksportuj bazę do pliku!</el-button>
                </el-form>
            </el-row>
        </el-form>
    </div>
</template>
<script>
    import * as FileSaver from "file-saver";

    export default {
        name: 'export',
        data(){
            return {
                componentName: 'Export',
                mydata: [],
            }
        },
        methods: {
            handleExport: function(){
                this.$axios
                    .get(this.$api + "/admin/export",
                        {headers: {'Content-Type': 'application/json'}})
                    .then(resp => {
                        if(resp.data!=="") {
                            var blob = new Blob([JSON.stringify(resp.data)], {type: "text/plain;charset=utf-8"});
                            console.log(blob);
                            FileSaver.saveAs(blob, "booksDB.txt");
                            this.$message({
                                message: "Pomyślnie utworzono plik z bazą danych",
                                type: "success"
                            })
                        } else {
                            this.$message({
                                message:"Plik jest pusty",
                                type:"error"
                            })
                        }
                    })
                    .catch(error => {
                        console.log(error)
                    })
            },
        },
        mounted: function() {
        },

    }
</script>
<style lang="scss">
    #export{
        text-align:center;
        margin:auto;
    }

</style>