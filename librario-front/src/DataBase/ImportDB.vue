<template>
    <div id="importDB">
        <h3>
            Importuj Bazę książek z pliku
        </h3>

        <el-form>
            <el-row>
                <el-form ref="importDB">
                        <el-upload
                                class="upload-demo"
                                ref="upload"
                                action="http://localhost:8081/admin/import"
                                :auto-upload="false"
                                multiple
                                with-credentials="true"
                                :limit="1"
                                :on-success="handleFileUploadSuccess"
                                :on-error="handleFileUploadError">
                            <el-button slot="trigger" size="small" type="primary">Wybierz plik</el-button>
                            <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">Wyślij do bazy!</el-button>
                            <div class="el-upload__tip" slot="tip">Plik musi mieć format *.txt</div>
                        </el-upload>
                </el-form>
            </el-row>
        </el-form>
    </div>
</template>
<script>
    export default {
        name: 'importDB',
        data(){
            return {
                componentName: 'ImportDB',
            }
        },
        methods: {
            handleFileUploadSuccess: function(data, file){
                this.$message({
                    message: "Pomyślnie dodano bazę",
                    type: "success"
                });
            },
            handleFileUploadError: function(error, vv){
                this.$message({message: error, type: "error"})
            },
            submitUpload() {
                this.$refs.upload.submit();
            }
        },
        mounted: function() {
        },

    }
</script>
<style lang="scss">
    #importDB{
        text-align:center;
        margin:auto;
    }

</style>