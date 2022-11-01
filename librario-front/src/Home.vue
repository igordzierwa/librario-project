<template>

    <div id="AGHSSIE">


    <el-row>
        <h2>
            Zaloguj się, aby uzyskać dostęp do zawartości strony
        </h2>
    </el-row>
    </div>
</template>

<script>

    export default {
        components: {},
        name: 'home',
        data () {
            return {
                name: "LibrarioFront",
                msg: 'Hello world!',
                model: {
                },
                rules: {
                    mail: [
                        {required: true, type: 'email', message: "", trigger: 'change'}
                    ],
                    firstname: [
                        {required: true, message: "", trigger: 'change'}
                    ],
                    lastname: [
                        {required: true, message: "", trigger: 'change'}
                    ],
                    password: [
                        {required: true, message: "", trigger: 'change'}
                    ],
                }
            }

        },
        computed: {
            //metody computed działają tak, że obserwują wszystkie zależne zmienne
            // i metody i w momencie kiedy jakaś się zaktualizuje, metoda computed się wywołuje
            metodaComputed(){
                /*
                if(this.innaMetodaComputed == "CosTam" && this.model.cosTam == 2)
                    return true
                return false
                */
            },
            innaMetodaComputed(){
                return true;
            }
        },


        methods: {
            //zwykłe metody - wywoływane ręcznie
            //służa np do obsługiwania clicków w jakies butony, albo obsługe zmiany selecta itp
            metoda(){
                //cos tam robi
            },

            valid(){
                //to jest metoda walidująca cały komponent formularza - na razie nevermind
                var valid_value = false;
                this.$refs[this.name].validate((valid) => {
                    if (valid) {
                        valid_value = true;
                        return true;
                    } else {
                        return false;
                    }
                });
                return valid_value;
            },
            validate() {
                return new Promise((resolve, reject) => {
                    this.$refs.OneStepBasket.validate((valid) => {
                        this.$emit('on-validate', valid, this.model)
                        resolve(valid);
                    });
                })
            }
        },
        created(){
            console.log("created");
            // wywoływana automatycznie po stworzeniu aplikacji

        },

        mounted () {
            console.log("mounted")
            //metoda wywolywana automatycznie po metodzie created()
            //można tu zrobic rzeczy takie jak np sprawdzenie czy uzytkonik jest zalogowany czy przekierować go na stronę logowania

        },
    }
</script>

<style lang="scss">
    @import url('https://fonts.googleapis.com/css?family=Roboto:300,400,700&display=swap');
    body{
        font-family: 'Roboto', sans-serif;
        font-weight: 400;
    }
    #AGHSSIE{
        text-align:center;
        margin:auto;
    }
</style>

