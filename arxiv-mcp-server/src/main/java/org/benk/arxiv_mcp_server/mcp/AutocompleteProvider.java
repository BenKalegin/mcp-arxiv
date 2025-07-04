package org.benk.arxiv_mcp_server.mcp;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AutocompleteProvider {

    private final Map<String, List<String>> countryDatabase = new HashMap<>();

    private final Map<String, List<String>> usernameDatabase = new HashMap<>();

    public AutocompleteProvider() {

        usernameDatabase.put("a", List.of("alex123", "admin", "alice_wonder", "andrew99"));
        usernameDatabase.put("b", List.of("bob_builder", "blue_sky", "batman", "butterfly"));
        usernameDatabase.put("c", List.of("charlie", "cool_cat", "coder42", "captain_marvel"));
        usernameDatabase.put("d", List.of("david_dev", "dragon_slayer", "diamond_hand", "dancer"));
        usernameDatabase.put("j", List.of("john_doe", "java_expert", "jazz_lover", "jupiter"));
        usernameDatabase.put("m", List.of("martin123", "moon_walker", "master_chef", "music_fan"));
        usernameDatabase.put("s", List.of("sarah", "super_coder", "star_gazer", "swift_dev"));
        usernameDatabase.put("t", List.of("tech_guru", "traveler", "tiger", "tester101"));

        countryDatabase.put("a", List.of("Afghanistan", "Albania", "Algeria", "Argentina", "Australia", "Austria"));
        countryDatabase.put("b", List.of("Bahamas", "Belgium", "Brazil", "Bulgaria"));
        countryDatabase.put("c", List.of("Canada", "Chile", "China", "Colombia", "Croatia"));
        countryDatabase.put("f", List.of("Finland", "France"));
        countryDatabase.put("g", List.of("Germany", "Greece"));
        countryDatabase.put("i", List.of("Iceland", "India", "Indonesia", "Ireland", "Italy"));
        countryDatabase.put("j", List.of("Japan"));
        countryDatabase.put("u", List.of("Uganda", "Ukraine", "United Kingdom", "United States"));
    }

//    /**
//     * Complete method for usernames in a user status prompt.
//     */
//    @McpComplete(uri = "user-status://{username}")
//    public List<String> completeUsername(String usernamePrefix) {
//        String prefix = usernamePrefix.toLowerCase();
//        if (prefix.isEmpty()) {
//            return List.of("Enter a username");
//        }
//
//        String firstLetter = prefix.substring(0, 1);
//        List<String> usernames = usernameDatabase.getOrDefault(firstLetter, List.of());
//
//        return usernames.stream().filter(username -> username.toLowerCase().startsWith(prefix)).toList();
//    }
//
//    @McpComplete(prompt = "personalized-message")
//    public List<String> completeName(String name) {
//        String prefix = name.toLowerCase();
//        if (prefix.isEmpty()) {
//            return List.of("Enter a username");
//        }
//
//        String firstLetter = prefix.substring(0, 1);
//        List<String> usernames = usernameDatabase.getOrDefault(firstLetter, List.of());
//
//        return usernames.stream().filter(username -> username.toLowerCase().startsWith(prefix)).toList();
//    }
//
//    /**
//     * Complete method for country names in a travel prompt.
//     */
//    @McpComplete(prompt = "travel-planner")
//    public CompleteResult completeCountryName(CompleteRequest request) {
//        String prefix = request.argument().value().toLowerCase();
//        if (prefix.isEmpty()) {
//            return new CompleteResult(new CompleteCompletion(List.of("Enter a country name"), 1, false));
//        }
//
//        String firstLetter = prefix.substring(0, 1);
//        List<String> countries = countryDatabase.getOrDefault(firstLetter, List.of());
//
//        List<String> matches = countries.stream()
//                .filter(country -> country.toLowerCase().startsWith(prefix))
//                .toList();
//
//        return new CompleteResult(new CompleteCompletion(matches, matches.size(), false));
//    }
}
