import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { register } from "../services/authService";

export default function RegisterPage() {
    const navigate = useNavigate();

    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [password, setPassword] = useState("");

    const [errorMsg, setErrorMsg] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMsg("");

        try {
            await register(username, email, password, phoneNumber);
            window.dispatchEvent(new Event("userChanged"));

            console.log("Register successfully by ", username);
            navigate("/mon-compte");
        } catch (err) {
            console.log(err);

            if (!err.response) {
                setErrorMsg("Serveur injoignable !");
                return;
            }

            switch (err.response.status) {
                case 400:
                    setErrorMsg("Mot de passe invalide ! Minimum 8 caractères, 1 maj, 1 min, 1 chiffre, 1 symbole.");
                    break;
                case 409:
                case 500:
                    setErrorMsg("Email ou nom d’utilisateur déjà utilisé !");
                    break;
                default:
                    setErrorMsg("Erreur inconnue lors de l’inscription !");
            }
        }
    };

    return (
        <div className="flex flex-col items-center justify-center py-20 bg-gradient-to-b from-purple-50 to-white">
            <div className="bg-white shadow-lg rounded-xl p-8 w-96">
                <h2 className="text-2xl font-bold text-center text-purple-700 mb-6">
                    Inscription
                </h2>

                {errorMsg && (
                    <p className="text-red-500 text-center mb-3 font-medium">
                        {errorMsg}
                    </p>
                )}

                <form className="space-y-4" onSubmit={handleSubmit}>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Nom d’utilisateur</label>
                        <input
                            type="text"
                            className="w-full mt-1 p-2 border rounded-lg focus:ring-2 focus:ring-purple-500"
                            placeholder="Votre pseudo"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Email</label>
                        <input
                            type="email"
                            className="w-full mt-1 p-2 border rounded-lg focus:ring-2 focus:ring-purple-500"
                            placeholder="exemple@email.com"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Téléphone</label>
                        <input
                            type="tel"
                            className="w-full mt-1 p-2 border rounded-lg focus:ring-2 focus:ring-purple-500"
                            placeholder="+33 6 00 00 00 00"
                            value={phoneNumber}
                            onChange={(e) => setPhoneNumber(e.target.value)}
                            required
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Mot de passe</label>
                        <input
                            type="password"
                            className="w-full mt-1 p-2 border rounded-lg focus:ring-2 focus:ring-purple-500"
                            placeholder="********"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    <button
                        type="submit"
                        className="w-full bg-purple-600 text-white py-2 rounded-lg hover:bg-purple-700"
                    >
                        S’inscrire
                    </button>
                </form>

                <p className="text-center text-sm text-gray-500 mt-4">
                    Déjà un compte ?{" "}
                    <Link to="/login" className="text-purple-600 hover:underline">
                        Se connecter
                    </Link>
                </p>
            </div>
        </div>
    );
}
